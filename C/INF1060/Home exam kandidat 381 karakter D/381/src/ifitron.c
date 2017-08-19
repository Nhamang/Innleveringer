#include <stdio.h>
#include <stdlib.h>
#include <getopt.h>     /* For getopt_long() */

#include "engine.h"
#include "player.h"

void args_handle (int argc, char* argv[]);
void print_help();

int port=0;
char* addr;
int mport=0;
char* maddr;


/** Handles all the commandline arguments.
 *
 * This is the main function of this module. It processes
 * an argv[] array of argc elements based on the options
 * specified at the struct option.
 *
 * If you want to add another argument, follow these steps:
 * # Add an option on the struct option.
 * # Add the short equivalent (if it exists) on the getopt_long() call.
 * # Add a case on the switch specifying the action of that option.
 */


void args_handle (int argc, char* argv[])
{
	static struct option options[] =
		{
			{"help",         no_argument, NULL, 'h'},
			{"user", required_argument, NULL, 'u'},
			{"highscoreserveraddr", required_argument, NULL, 's'},
			{"highscoreserverport", required_argument, NULL, 'p'},
			{"multiplayerserverport", required_argument, NULL, 'm'},
			{"multiplayerserveraddress", required_argument, NULL, 'a'},
			/* The last element must be all zeroes */
			{0, 0, 0, 0}
		};
		/* The index of the current option */
		int option_index;
		/* The character for comparison */
		int c = 0;


		/* We keep checking the arguments untill they run out (c == -1) */
		while (c != -1)
		{
			c = getopt_long (argc, argv, "h;u;m;hp;hs", options, &option_index);

			switch (c)
			{
			case -1:
			  // There were no '-' parameters passed
			  // or all the parameters were processed
			  break;

			case 'h':
			default:
			  print_help();
			  exit(EXIT_SUCCESS);
			  break;

			//Setting of username
			case 'u':
				if(argv[optind]){
					settID(argv[optind]);
				}
				else{
					printf("\nUsername missing\n");
					exit(1);
				}
				break;

			
			case 'm':
				if(optind){
					mport=atoi(argv[optind]);
				}else{
					ifitron_abort("\nMissing multiplayer port\n");
				}
				
				if(maddr!=NULL){
					init_multi(maddr, mport);
				}
				break;

			case 'a':
				if(optind){
					maddr=argv[optind];
				}else{
					ifitron_abort("\nMissing multiplayer address\n");
				}
				break;

			//Sets address for highscoreserver
			case 's':
				if(optind){
					addr=argv[optind];
				}else {
					ifitron_abort("\nMissing Address\n");
				}
				break;

			//sets port for highscoreserver
			case 'p':
				if (optind){
					port=atoi(argv[optind]);
				}else{
					ifitron_abort("\nMissing port\n");
				}
				if (addr!=NULL){
					init_highscore(addr, port);
				}
				break;
			}
		}

		/* Just in case the user specified more arguments (not options)
		 * than needed, you decide what to do. Here, we just ignore them */
		while (optind < argc) optind++;
}


/**	Prints Help instructions on standard output.
 */
void print_help(void)
{
  printf("Synopsis:\n");
  printf("\tThe classic TRON game.\n\n");

  printf("Controls:\n");
  printf("\tNumbers (1 ~ 9)   Changes the game speed at the main menu\n");
  printf("\tArrow Keys        Control the directions\n");
  printf("\tq                 Quits the game at any time\n");
  printf("\n\n");

  printf("Usage:\n");
  printf("\tifitron [-h]\n");
  printf("Commandline arguments:\n\n");
  printf("\t-h, --help         Displays the help guidelines.\n");
  printf("\t-u, --user 		   Set username.\n");
  printf("\t-s, --highscoreserveraddr, Set highscore ipaddress.\n");
  printf("\t-p, --highscoreserverport, Set highscore port.\n");
  printf("\t-m,  --multiplayerserveraddr, Set multiplayer ipaddress.\n");
  printf("\t-a,  --multiplayerserverport, Set multiplayer port.\n");
  printf("\n");
}



// The main loop of the game.

 int main (int argc, char* argv[])
{
	if (argc > 1) args_handle (argc, argv);
	if (argc < 1) ifitron_abort("\n Missing parameters \n");
	//if (id==NULL){
	//	ifitron_abort("\nMissing username\n");
	//}

	engine_init ();
	engine_show_main_menu ();
	ifitron_init ();
	
	while (TRUE)
	{
	  if (tron.is_alive == FALSE) ifitron_game_over ();

	  engine_get_game_input();
	  player_update();
	  
	  player_increase_size (1);
	  player_increase_score (game.level);
	  
	  if (tron.score % 50 == 0 && game.level < 9) game.level++;

	  if (player_hit_self() == TRUE  || player_hit_borders() == TRUE)
	    tron.is_alive = FALSE;

	  engine_show_screen ();
	}
	return 0;
}
