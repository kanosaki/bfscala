#include <stdio.h>
#include <stdlib.h>

#define INIT_MEMSIZE 1024

int ptr = 0;
int** memory = ensure_memory(INIT_MEMSIZE);
int memsize = INIT_MEMSIZE;

void
ensure_memory(int** mem, size_t size) {
    if(mem != NULL)
        free(mem);
    mem = (int**) malloc(size);
}

void
check_memory(void){
    if (memsize <= ptr) {
        memsize *= 2;
        ensure_memory(memory, memsize);
    }
}

void
check_ptr(void) {
    if(ptr < 0) {
        printf("RuntimeError: negative pointer\n");
        exit(EXIT_FAILURE);
    }
}

int
main(int argc, char** argv) {
    // + 
    *memory[pc] += n;

    // -
    *memory[ptr] -= n;

    // >
    ptr += n; check_memory();

    // <
    ptr -= n; check_ptr();

    // .
    putchar(*memory[ptr]);

    // ,
    *memory[ptr] = getchar();

}

