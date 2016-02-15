all: netpbm fout draw
	gcc main.c gfx_utils/netpbm.o gfx_utils/fout.o gfx_utils/draw.o -I gfx_utils/ -o main.out

netpbm:
	gcc -c gfx_utils/netpbm.c -o gfx_utils/netpbm.o

fout:
	gcc -c gfx_utils/fout.c -o gfx_utils/fout.o

draw:
	gcc -c gfx_utils/draw.c -o gfx_utils/draw.o

clean:
	rm -f gfx_utils/*.o
	rm -f *.ppm
	rm -f *.out
