all: netpbm fout
	gcc picmaker.c gfx_utils/netpbm.o gfx_utils/fout.o -I gfx_utils/ -o picmaker.out

netpbm:
	gcc -c gfx_utils/netpbm.c -o gfx_utils/netpbm.o

fout:
	gcc -c gfx_utils/fout.c -o gfx_utils/fout.o

clean:
	rm -f gfx_utils/netpbm.o
	rm -f gfx_utils/fout.o
	rm -f *.ppm
	rm -f *.out

run: picmaker.out
	./picmaker.out
