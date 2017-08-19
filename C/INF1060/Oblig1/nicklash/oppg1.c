/**
 * Oppg1.c
 * INF1060 obligatorisk oppgave 1.
 *
 * Skrevet av Nicklas M. Hamang 
 * Brukernavn Nicklash
 * Slitter med ØÆÅ i bytt vokal
 **/

#include <time.h>
#include <stdlib.h>
#include <string.h>
#include <stdio.h>

#define MAX_lengde 300

static int lengde(char*);
static void printlinjer();
static void tilfeldig();
static void fjernVokal();
static void byttVokal();


struct liste_noder{
  struct liste_noder *neste;
  char *linje;
};

  typedef struct liste_noder liste;
liste *forste = NULL;
liste *siste = NULL;

//Antall linjer i fil
int antLinjer = 0;

liste * start;

//definerer hva en vokal er
char vokal[] = "aeiouyæøå";


int main(int argc, char *argv[]){

  FILE *inn = fopen(argv[2], "r");

  unsigned char naa;
  int j = 0;
  char *ordre = argv[1];
  if(inn == NULL){
    printf("Oppsto en feil");
      return(1);
  }


  liste * temp;

  start = temp = (liste*)malloc(sizeof(liste));
  temp->linje = malloc(sizeof(char) * MAX_lengde);

  antLinjer++;



  naa = fgetc(inn);
  while(!feof(inn)){
    if(naa == '\n'){
      temp->linje[j] = '\0';
      temp->neste = (liste*)malloc(sizeof(liste));
      temp->neste->linje = malloc(sizeof(char) * MAX_lengde);
      antLinjer++;
      temp = temp->neste;

      j = 0;
    }
    else{
      temp->linje[j] = naa;
      j++;
    }
    naa = getc(inn);
  }
  //leser kommando og sender til riktig metode.
  if(strncmp(ordre, "print", 5) == 0){
    printlinjer();
  }
  else if(strncmp(ordre, "random", 6) == 0){
    tilfeldig();
  }
  else if(strncmp(ordre, "replace", 7) == 0){
    byttVokal();
  }
  else if(strncmp(ordre, "remove", 6) == 0){
    fjernVokal();
  }
  else if(strncmp(ordre, "len", 3) == 0){
    printf("lengden på denne teksten er: %d \n", lengde(argv[2]));
  }
  else {
    printf("det oppsto en feil");
  }
  return 0;
}
//sjekker lengden på filen
static int lengde(char *fil){

  FILE *navn = fopen(fil, "r");

  int antTegn = 0;

  if(navn != NULL){
    while (fgetc(navn) != EOF){
      antTegn++;
    }
  }

  else{
    printf("det oppsto en feil \n");
  }

  fclose(navn);
  return antTegn;
}

//printer ut innfilen
static void printlinjer(){

  liste * print = start;
  //går igjennom hele filen å skriver ut en og en linje
  while(print != NULL){

    printf("%s\n", print->linje);
    print = print->neste;
  }
}
//skriver ut en tilfeldig linje
static void tilfeldig(){

  srand(time(NULL));

  int tilfeldigLinje = (int) rand() % (antLinjer-1);

  liste * tilfeldigPrinting = start;

  int j;

  for(j=0; j<tilfeldigLinje; j++){

    tilfeldigPrinting = tilfeldigPrinting->neste;
  }

  //printer ut den tilfeldige linjen
  printf("%s\n", tilfeldigPrinting->linje);
}


//bytter ut alle vokalen
static void byttVokal(){
  //bestemmer plasseringen
  int plass;
  
  for(plass = 0; plass<sizeof(vokal); plass++){
    liste * denne = start;
    
    printf("Byttet med '%c'\n", vokal[plass]);

    while(denne != NULL){

      char linje[MAX_lengde];
      char *pl = linje;
      strcpy(linje, denne->linje);
      
      while(*pl) {

	if (strchr(vokal, *pl) > 0){
	  *pl = vokal[plass];
	}

	pl++;
      }

      printf("%s\n", linje);
      denne = denne->neste;
    }
  }
}


//Fjerner vokal
static void fjernVokal(){

  liste * denne = start;
  while (denne != NULL){

    char linje[MAX_lengde];
    int pos = 0;
    int j;


    for(j=0; j<strlen(denne->linje); j++){
      if(strchr(vokal, denne->linje[j]) == NULL){
	  linje[pos++] = denne->linje[j];
	}
    }

    linje[pos] = '\0';
    printf("\n%s", linje);
    denne = denne->neste;
  }
}


