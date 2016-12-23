#include <stdint.h>
#include <stdio.h>
#include <stdbool.h>
#include <stdlib.h>

int32_t a = 0;
int32_t b = 0;
int32_t c = 0;
int32_t d = 0;
bool toggledTheJump = false;

void toggle(int32_t target) {
  // tgl c with c = 2 means toggle pc 18, which is the jump back to lbl
  // is this horrible or what :D
  if (target == 2) {
    toggledTheJump = true;
  }
}

int main(int argc, char** argv) {
  a = atoi(argv[1]);
  b = a;
  b--;

lbl:
  a *= b;
  b--;
  c = b * 2;
  toggle(c);
  if (!toggledTheJump) {
    goto lbl;
  }
  
  a += 78 * 75;

  printf("%d\n", a);
}
