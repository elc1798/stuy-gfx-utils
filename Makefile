all: netpbm fout draw matrix
	gcc main.c gfx_utils/netpbm.o gfx_utils/fout.o gfx_utils/draw.o gfx_utils/matrix.o -I gfx_utils/ -lm -o main.out

netpbm:
	gcc -c gfx_utils/netpbm.c -lm -o gfx_utils/netpbm.o

fout:
	gcc -c gfx_utils/fout.c -o gfx_utils/fout.o

draw:
	gcc -c gfx_utils/draw.c -lm -o gfx_utils/draw.o

matrix:
	gcc -c gfx_utils/matrix.c -lm -o gfx_utils/matrix.o

run: all
	./main.out
	display pic1.ppm

clean:
	rm -f gfx_utils/*.o
	rm -f *.ppm
	rm -f *.out
