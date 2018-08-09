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

  float ac = in[addr(width, height, lx-1, ly-1)+shift] * (-1)+
          in[addr(width, height, lx  , ly-1)+shift] * 0+
          in[addr(width, height, lx+1, ly-1)+shift] * 1+

          in[addr(width, height, lx-1, ly)+shift] * (-2)+
          in[addr(width, height, lx  , ly)+shift] * 0+
          in[addr(width, height, lx+1, ly)+shift] * 2+

          in[addr(width, height, lx-1, ly+1)+shift] * (-1)+
          in[addr(width, height, lx  , ly+1)+shift] * 0+
          in[addr(width, height, lx+1, ly+1)+shift] * 1;

  float bc = in[addr(width, height, lx-1, ly-1)+shift] * (-1)+
          in[addr(width, height, lx  , ly-1)+shift] * (-2)+
          in[addr(width, height, lx+1, ly-1)+shift] * (-1)+

          in[addr(width, height, lx-1, ly)+shift] * 0+
          in[addr(width, height, lx  , ly)+shift] * 0+
          in[addr(width, height, lx+1, ly)+shift] * 0+

          in[addr(width, height, lx-1, ly+1)+shift] * 1+
          in[addr(width, height, lx  , ly+1)+shift] * 2+
          in[addr(width, height, lx+1, ly+1)+shift] * 1;

  return ac*ac + bc*bc;
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
  float samp = amp/1600;
  int oadd = (ly*width+lx)*4;
  outb[oadd  ]= bound(filter1(in,width,height,lx,ly,0)*samp);
  outb[oadd+1]= bound(filter1(in,width,height,lx,ly,1)*samp);
  outb[oadd+2]= bound(filter1(in,width,height,lx,ly,2)*samp);
  outb[oadd+3]= 255;
}
