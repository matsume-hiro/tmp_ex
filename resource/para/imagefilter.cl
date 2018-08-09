#pragma OPENCL EXTENSION cl_khr_byte_addressable_store : enable

int addr(const int width, const int height, int x, int y){
  if(y<0){y=0;}
  if(height-1<y){y=height-1;}
  if(x<0){x=0;}
  if(width-1<x){x=width-1;}
  return (y*width*3+x*3);
}

float bound(const float in){
  if(in<0) return 0; 
  if(in>255) return 255.0f;
  return in;
}

float filter1(__global const uchar* in, const int width, const int height,
           const int lx,const int ly, const int shift){
  return (
          in[addr(width, height, lx-1, ly-1)+shift] * (0.7f)+
          in[addr(width, height, lx  , ly-1)+shift] * (1)+
          in[addr(width, height, lx+1, ly-1)+shift] * (0.7f)+

          in[addr(width, height, lx-1, ly)+shift] * (1)+
          in[addr(width, height, lx  , ly)+shift] * 1.2f+
          in[addr(width, height, lx+1, ly)+shift] * (1)+

          in[addr(width, height, lx-1, ly+1)+shift] * (0.7f)+
          in[addr(width, height, lx  , ly+1)+shift] * (1)+
          in[addr(width, height, lx+1, ly+1)+shift] * (0.7f)
          )/8.0;
}

float filter2(__global const uchar* in, const int width, const int height,
           const int lx,const int ly, const int shift){
  return (
          in[addr(width, height, lx-1, ly-1)+shift] * 0+
          in[addr(width, height, lx  , ly-1)+shift] * 1+
          in[addr(width, height, lx+1, ly-1)+shift] * 0+

          in[addr(width, height, lx-1, ly)+shift] * 1+
          in[addr(width, height, lx  , ly)+shift] * (-4)+
          in[addr(width, height, lx+1, ly)+shift] * 1+

          in[addr(width, height, lx-1, ly+1)+shift] * 0+
          in[addr(width, height, lx  , ly+1)+shift] * 1+
          in[addr(width, height, lx+1, ly+1)+shift] * 0
          );
}


float filter3(__global const uchar* in, const int width, const int height,
           const int x,const int y, const int shift, float scale){
  float tmp=0;
  float sum=0;
  int r = (int)(scale*3.0f);
  for(int ly=-r;ly<=r;ly++){
    for(int lx=-r;lx<=r;lx++){
      float w = (float)exp((float)(-(ly*ly+lx*lx)/(scale*scale)));
      sum += w;
      tmp += in[addr(width, height, x+lx, y+ly)+shift] * w;
    }
  }
  return tmp/sum; 
}

float filter4(__global const uchar* in, const int width, const int height,
              const int x,const int y, const int shift, int cx, int cy){
  float tmp=0;
  float sum=0;
  float r2 = (cx-x)*(cx-x)+(cy-y)*(cy-y);
  if(r2<2500){  
    return in[addr(width, height, x, y)+shift];
  }
  return 0; 
}

float filter5(__global const uchar* in, const int width, const int height,
              const int x,const int y, const int shift, int cx, int cy){
  float sum=0;
  int dxi = x-cx;
  int dyi = y-cy;
  float tmp = (float)-(dxi*dxi+dyi*dyi)*0.0005f;
  float w = (float)exp(-(dxi*dxi+dyi*dyi)*0.0005f)+1.0f;
  float nx = cx+dxi*w;
  float ny = cy+dyi*w;
  float ldx = nx-floor(nx);
  float rdx = 1-ldx;
  float udy = ny -floor(ny); 
  float ldy = 1-udy;
  int lx = floor(nx);
  int ly = floor(ny);
  return(in[addr(width, height, lx,  ly  )+shift]*rdx*ldy+
         in[addr(width, height, lx+1,ly  )+shift]*ldx*ldy+
         in[addr(width, height, lx,  ly+1)+shift]*rdx*udy+
         in[addr(width, height, lx+1,ly+1)+shift]*ldx*udy);
}

void insertionsort(uchar* data, int n){
  int i,j;
  uchar tmp;
  for (i = 1; i < n; i++) {
    tmp = data[i];
    if (data[i-1] > tmp) {
      j = i;
      do {
        data[j] = data[j-1];
        j--;
      } while (j > 0 && data[j-1] > tmp);
      data[j] = tmp;
    }
  }
}

uchar filter6(__global const uchar* in, const int width, const int height,
              const int x,const int y, const int shift, int cx){
  // cx should be 0 to 8
  uchar data[9];               
  data[0]=in[addr(width, height, x-1, y-1)+shift];
  data[1]=in[addr(width, height, x  , y-1)+shift];
  data[2]=in[addr(width, height, x+1, y-1)+shift];
  data[3]=in[addr(width, height, x-1, y  )+shift];
  data[4]=in[addr(width, height, x  , y  )+shift];
  data[5]=in[addr(width, height, x+1, y  )+shift];
  data[6]=in[addr(width, height, x-1, y+1)+shift];
  data[7]=in[addr(width, height, x  , y+1)+shift];
  data[8]=in[addr(width, height, x+1, y+1)+shift];
  insertionsort(data,9);
  return(data[cx]);
}


// OpenCL Kernel Function 
__kernel void Filter1(const int width, const int height, 
                     __global const uchar* in, 
                     __global uchar *outb,
		     const float amp) {
  // get index of global data array
  int lx = get_global_id(0);
  int ly = get_global_id(1);
/*
  // bound check (equivalent to the limit on a 'for' loop for standard/serial C code
  if (lx > width || ly >height)  {
    return;
  }
*/
  float samp = amp/15;
  int oadd = (ly*width+lx)*4;
  outb[oadd  ]= bound(filter1(in,width,height,lx,ly,0)*samp);
  outb[oadd+1]= bound(filter1(in,width,height,lx,ly,1)*samp);
  outb[oadd+2]= bound(filter1(in,width,height,lx,ly,2)*samp);
  outb[oadd+3]= 255;
}

// OpenCL Kernel Function 
__kernel void Filter2(const int width, const int height, 
                     __global const uchar* in, 
                     __global uchar *outb,
		     const float scale) {
  // get index of global data array
  int lx = get_global_id(0);
  int ly = get_global_id(1);

  // bound check (equivalent to the limit on a 'for' loop for standard/serial C code
  if (lx > width || ly >height)  {
    return;
  }

  float samp = scale/50; 
  int add = addr(width,height,lx,ly);
  int oadd = (ly*width+lx)*4;
  outb[oadd  ]= bound(filter2(in,width,height,lx,ly,0)*samp+128);
  outb[oadd+1]= bound(filter2(in,width,height,lx,ly,1)*samp+128);
  outb[oadd+2]= bound(filter2(in,width,height,lx,ly,2)*samp+128);

  outb[oadd  ]= bound(in[add]+filter2(in,width,height,lx,ly,0)*samp);
  outb[oadd+1]= bound(in[add+1]+filter2(in,width,height,lx,ly,1)*samp);
  outb[oadd+2]= bound(in[add+2]+filter2(in,width,height,lx,ly,2)*samp);

  outb[oadd+3]= 255;
}

// OpenCL Kernel Function 
__kernel void Filter3(const int width,const int height, 
                     __global const uchar* in, 
                     __global uchar *outb,
		     const float scale) {

  // get index of global data array
  int lx = get_global_id(0);
  int ly = get_global_id(1);

/*
  // bound check (equivalent to the limit on a 'for' loop for standard/serial C code
  if (lx > width || ly >height)  {
    return;
  }
*/
  float sscale = scale/10;
  int oadd = (ly*width+lx)*4;
  outb[oadd  ]= bound(filter3(in,width,height,lx,ly,0, sscale));
  outb[oadd+1]= bound(filter3(in,width,height,lx,ly,1, sscale));
  outb[oadd+2]= bound(filter3(in,width,height,lx,ly,2, sscale));
  outb[oadd+3]= 255;
}

__kernel void Filter4(const int width, const int height, 
                     __global const uchar* in, 
                     __global uchar *outb,
                      const float cx) {
  // get index of global data array
  int lx = get_global_id(0);
  int ly = get_global_id(1);

/*
  // bound check (equivalent to the limit on a 'for' loop for standard/serial C code
  if (lx > width || ly >height)  {
    return;
  }
*/
  int oadd = (ly*width+lx)*4;
  outb[oadd  ]= bound(filter4(in,width,height,lx,ly,0, (int)(cx*1.25f),120));
  outb[oadd+1]= bound(filter4(in,width,height,lx,ly,1, (int)(cx*1.25f),120));
  outb[oadd+2]= bound(filter4(in,width,height,lx,ly,2, (int)(cx*1.25f),120));
  outb[oadd+3]= 255;
}

__kernel void Filter5(const int width, const int height, 
                     __global const uchar* in, 
                     __global uchar *outb,
		     const float cx) {
  // get index of global data array
  int lx = get_global_id(0);
  int ly = get_global_id(1);

/*
  // bound check (equivalent to the limit on a 'for' loop for standard/serial C code
  if (lx > width || ly >height)  {
    return;
  }
*/
  int oadd = (ly*width+lx)*4;
  outb[oadd  ]= bound(filter5(in,width,height,lx,ly,0, cx*1.25f,120));
  outb[oadd+1]= bound(filter5(in,width,height,lx,ly,1, cx*1.25f,120));
  outb[oadd+2]= bound(filter5(in,width,height,lx,ly,2, cx*1.25f,120));
  outb[oadd+3]= 255;
}

__kernel void Filter6(const int width, const int height, 
                     __global const uchar* in, 
                     __global uchar *outb,
		     const float cx) {
  // get index of global data array
  int lx = get_global_id(0);
  int ly = get_global_id(1);

/*
  // bound check (equivalent to the limit on a 'for' loop for standard/serial C code
  if (lx > width || ly >height)  {
    return;
  }
*/
  int n = cx/32;
  int oadd = (ly*width+lx)*4;
  outb[oadd  ]= filter6(in,width,height,lx,ly,0, n);
  outb[oadd+1]= filter6(in,width,height,lx,ly,1, n);
  outb[oadd+2]= filter6(in,width,height,lx,ly,2, n);
  outb[oadd+3]= 255;
}

__kernel void Filter7(const int width, const int height, 
                     __global const uchar* in, 
                     __global uchar *outb,
		     const float cx) {
  // get index of global data array
  int lx = get_global_id(0);
  int ly = get_global_id(1);

/*
  // bound check (equivalent to the limit on a 'for' loop for standard/serial C code
  if (lx > width || ly >height)  {
    return;
  }
*/
  int oadd = (ly*width+lx)*4;
  int iadd = addr(width, height, lx+1, ly+1);
  uchar b= in[iadd  ];
  uchar g= in[iadd+1];
  uchar r= in[iadd+2];
  outb[oadd  ]= b;
  outb[oadd+1]= g;
  outb[oadd+2]= r;
  outb[oadd+3]= 255-(uchar)(max(max(b,g),r)*(cx/255));
}

__kernel void Filter8(const int width, const int height, 
                     __global const uchar* in, 
                     __global uchar *outb,
		     const float cx) {
  // get index of global data array
  int lx = get_global_id(0);
  int ly = get_global_id(1);

/*
  // bound check (equivalent to the limit on a 'for' loop for standard/serial C code
  if (lx > width || ly >height)  {
    return;
  }
*/
  int cxi = (int)cx/32;
  int oadd = (ly*width+lx)*4;
  int iadd = addr(width, height, lx+1, ly+1);
  outb[oadd  ]= (in[iadd+0]>>cxi)<<cxi;
  outb[oadd+1]= (in[iadd+1]>>cxi)<<cxi;
  outb[oadd+2]= (in[iadd+2]>>cxi)<<cxi;
  outb[oadd+3]= 255;
}

__kernel void Filter9(const int width, const int height, 
                     __global const uchar* in, 
                     __global uchar *outb,
		     const float cx) {
  // get index of global data array
  int lx = get_global_id(0);
  int ly = get_global_id(1);

/*
  // bound check (equivalent to the limit on a 'for' loop for standard/serial C code
  if (lx > width || ly >height)  {
    return;
  }
*/
  int cxi = (int)cx/16;
  int oadd = (ly*width+lx)*4;
  int iadd = addr(width, height, lx+1, ly+1);
  int g = (in[iadd+0]*0.072169f+in[iadd+1]*0.715160f+in[iadd+2]*0.212671f)/cx;
  g = g*cx;
  outb[oadd  ]= g;
  outb[oadd+1]= g;
  outb[oadd+2]= g;
  outb[oadd+3]= 255-g;
}
