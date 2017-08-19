/**
 * Obligatorisk oppgave 1 del 2.
 * INF1060 
 * Skrevet av Nicklas M. Hamang
 * Brukernavn Nicklash
 *
 *
 * Har et problem med encode.
 **/

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

static void print(char*);
static void decode(FILE*, FILE*);
static void encode(FILE*, FILE*);

int main(int argc, char*argv[]){

  //leser og lagrer filer
  FILE*innfil = fopen(argv[2], "r");
  FILE*utfil = fopen(argv[3], "w");
  //sjekker om inn filen finnes
  if(innfil == NULL){
    printf("Fant ikke fil: %s", argv[2]);
    return 1;
  }
  //leser inn kommandoen
  char*ordre = argv[1];

  //Sjekker hva kommandoen var og sender til riktig metode
  if(strncmp(ordre, "p", 1) == 0){
    printf("Printer: \n");
    print(argv[2]);
  }

  else if (strncmp(ordre, "e", 1) == 0){
    printf("Encoder");
    encode(innfil, utfil);
  }
	
  else if (strncmp(ordre, "d", 1) == 0){
    printf("Decoder");
    decode(innfil, utfil);
  }
	
  else {
    printf("kunne ikke opne filen: %s\n", argv[2]);
  }
	
	
  fclose(innfil);
  fclose(utfil);
  return 0;
	
}


//Printer ut innfilen
static void print(char*innfil){
		
  FILE*inn = fopen(innfil, "r");
  unsigned char temp;
	
  while(!feof(inn)){
    temp = fgetc(inn);
    printf("%c", temp);
  }
	
}

//encoder filen og lagrer den til en utfil. ser ut til å være en feil
static void encode(FILE *inn, FILE *ut){
  
  int bytt[4] = {6, 4, 2, 0};
  char les = 0;
  char skriv = 0;
  int j;
  //leser filen og ser hvilket tegn det er å bytter den til en byte
  while(!feof(inn)){
    for(j=0; j<4&&!feof(inn); j++){
      les = getc(inn);
      if(les == ' '){
	les = 0;
      }
      else if(les == ':'){
	les = 1;
      }
      else if(les == '@'){
	les = 2;
      }
      else if(les == '\n'){
	les = 3;
      }
      else{
	les = 0;
      }

      les = les<<bytt[j];
      skriv |= les;
    }
    fputc(skriv, ut);
    
  }
}

static void decode(FILE *inn, FILE *ut){
  
  char les2 = 0;
  char skriv2 = 0;
  int j;
  int bytter[4]={6, 4, 2, 0};

  while(!feof(inn)){
    char tegn[4] = {' ', ':', '@', '\n'};

    les2 = getc(inn);
    for(j=0; j<4; j++){
      skriv2 = les2 >> bytter[j] &3;
      printf("%c", tegn[skriv2]);
      //fputc(("%c", tegn[skriv2]), ut);
    }
  }

}
