#include <omp.h>
#include <openssl/aes.h>
#include <openssl/evp.h>
#include <stddef.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define AES_BLOCK_SIZE 16

void parallelAesEnc(unsigned char *input, int input_len, char *keyAscii,
                    unsigned char **output, int *totalLen) {
  unsigned char key[16];
  char firstByte[3];
  for (int i = 0; i < AES_BLOCK_SIZE; i++) {
    firstByte[0] = keyAscii[i * 2];
    firstByte[1] = keyAscii[i * 2 + 1];
    firstByte[2] = '\00';
    key[i] = (unsigned char)strtol(firstByte, NULL, 16);
  }
  int padding = input_len % AES_BLOCK_SIZE;
  int padLen = AES_BLOCK_SIZE - padding;
  int paddedLen = input_len + padLen;
  unsigned char *paddedInp = (unsigned char *)malloc(paddedLen);
  memcpy(paddedInp, input, input_len);
  memset(paddedInp + input_len, AES_BLOCK_SIZE - padding,
         (size_t)(AES_BLOCK_SIZE - (input_len % AES_BLOCK_SIZE)));
  *output = (unsigned char *)malloc(paddedLen);
  int blocks = paddedLen / AES_BLOCK_SIZE;
#pragma omp parallel
  {
    EVP_CIPHER_CTX *ctx = EVP_CIPHER_CTX_new();
    EVP_EncryptInit_ex(ctx, EVP_aes_128_ecb(), NULL, key, NULL);
    EVP_CIPHER_CTX_set_padding(ctx, 0);
#pragma omp for schedule(static)
    for (int block = 0; block < blocks; block++) {
      int outLen = 0;
      EVP_EncryptUpdate(ctx, *output + block * AES_BLOCK_SIZE, &outLen,
                        paddedInp + block * AES_BLOCK_SIZE, AES_BLOCK_SIZE);
    }
    EVP_CIPHER_CTX_free(ctx);
  }
  *totalLen = paddedLen;
  printf("Paralel enc %d", *totalLen);
}

void parallelAesDec(unsigned char *input, int input_len, char *keyAscii,
                    unsigned char **output, int *totalLen) {

  unsigned char key[16];
  char firstByte[3];
  for (int i = 0; i < AES_BLOCK_SIZE; i++) {
    firstByte[0] = keyAscii[i * 2];
    firstByte[1] = keyAscii[i * 2 + 1];
    firstByte[2] = '\00';
    key[i] = (unsigned char)strtol(firstByte, NULL, 16);
  }
  int padding = input_len % AES_BLOCK_SIZE;
  int padLen = AES_BLOCK_SIZE - padding;
  int paddedLen = input_len + padLen;
  unsigned char *paddedInp = (unsigned char *)malloc(paddedLen);
  memcpy(paddedInp, input, input_len);
  memset(paddedInp + input_len, AES_BLOCK_SIZE - padding,
         AES_BLOCK_SIZE - (input_len % AES_BLOCK_SIZE));
  *output = (unsigned char *)malloc(paddedLen);
  int blocks = paddedLen / AES_BLOCK_SIZE;
#pragma omp parallel
  {
    EVP_CIPHER_CTX *ctx = EVP_CIPHER_CTX_new();
    EVP_DecryptInit_ex(ctx, EVP_aes_128_ecb(), NULL, key, NULL);
    EVP_CIPHER_CTX_set_padding(ctx, 0);
#pragma omp for schedule(static)
    for (int block = 0; block < blocks; block++) {
      int outLen = 0;
      EVP_DecryptUpdate(ctx, *output + block * 16, &outLen,
                        paddedInp + block * 16, 16);
    }
    EVP_CIPHER_CTX_free(ctx);
  }
  *totalLen = paddedLen;
  printf("Paralel enc %d", *totalLen);
}

int main(int argc, char *argv[]) {
  if (argc < 4) {
    printf("Program needs a file path, a key and a mode");
    return EXIT_FAILURE;
  }

  unsigned char *output;
  int totalLen = 0;
  if (memcmp(argv[3], "dec", 3) == 0) {
    FILE *fInp = fopen(argv[1], "rb");
    fseek(fInp, 0, SEEK_END);
    int fSize = ftell(fInp);
    unsigned char *input = (unsigned char *)malloc(fSize);
    rewind(fInp);
    fread(input, 1, fSize, fInp);
    int totalSize = 0;
    parallelAesDec(input, fSize, argv[2], &output, &totalSize);
    char *extension = strchr(argv[1], '.');
    char *copyExtension = (char *)malloc(5);
    copyExtension[0] = '.';
    copyExtension[1] = 'd';
    copyExtension[2] = 'e';
    copyExtension[3] = 'c';
    copyExtension[4] = '\00';
    *extension = '\00';
    FILE *fEnc = fopen(strcat(argv[1], copyExtension), "wb");
    fwrite(output, 1, totalSize, fEnc);
  } else if (memcmp(argv[3], "enc", 3) == 0) {
    FILE *fInp = fopen(argv[1], "rb");
    fseek(fInp, 0, SEEK_END);
    int fSize = ftell(fInp);
    unsigned char *input = (unsigned char *)malloc(fSize);
    rewind(fInp);
    fread(input, 1, fSize, fInp);
    int totalSize = 0;
    parallelAesEnc(input, fSize, argv[2], &output, &totalSize);
    char *extension = strchr(argv[1], '.');
    char *copyExtension = (char *)malloc(5);
    copyExtension[0] = '.';
    copyExtension[1] = 'e';
    copyExtension[2] = 'n';
    copyExtension[3] = 'c';
    copyExtension[4] = '\00';
    *extension = '\00';
    FILE *fEnc = fopen(strcat(argv[1], copyExtension), "wb");
    fwrite(output, 1, totalSize, fEnc);
  }
  return EXIT_SUCCESS;
}
