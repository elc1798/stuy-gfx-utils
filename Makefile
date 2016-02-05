all: netpbm fout
	gcc example.c gfx_utils/netpbm.o gfx_utils/fout.o -I gfx_utils/ -o example.out

netpbm:
	gcc -c gfx_utils/netpbm.c -o gfx_utils/netpbm.o

fout:
	gcc -c gfx_utils/fout.c -o gfx_utils/fout.o

clean:
	rm -f gfx_utils/netpbm.o
	rm -f gfx_utils/fout.o
	rm -f *.ppm
	rm -f *.out
