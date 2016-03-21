help:
	@echo "'make interpret' will run with the interpretter'"
	@echo "'make old' will run the original main.c file (from 'Transformations' assignment)"

interpret:
	python interpreter.py test.pgi

all: netpbm fout draw matrix
	gcc .main.c gfx_utils/netpbm.o gfx_utils/fout.o gfx_utils/draw.o gfx_utils/matrix.o -I gfx_utils/ -lm -o main.out

old: netpbm fout draw matrix
	gcc old_main.c gfx_utils/netpbm.o gfx_utils/fout.o gfx_utils/draw.o gfx_utils/matrix.o -I gfx_utils/ -lm -o old_main.out
	./old_main.out
	display pic1.ppm

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
	display rendered.ppm

clean:
	rm -f gfx_utils/*.o
	rm -f *.ppm
	rm -f *.out
	rm -f .*.c
