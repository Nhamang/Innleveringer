#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ncurses.h> // the actual game engine
#include <unistd.h>  // for the usleep () function
#include <netinet/in.h>
#include <sys/socket.h>


#include "engine.h"
#include "player.h"
#include "netcom.h"

int high_score;
int multiplayer;
//struct packet pack;
struct topfive *top;

/** This SIMPLE formula determines a time (in microseconds) the game
 *  must wait between each screen refresh. It is based on the current
 *  game level, so the higher its value, the lowest is the delay and
 *  faster the game will appear to be. */

#define   REFRESH_DELAY     (50000 + ((9 - game.level) * 10000))


static char PLAYER_HEAD_CHAR = '@'; ///< The 'image' of the snake head
static char PLAYER_CHAR	     = 'o'; ///< The 'image' of the snake body
static char BORDER_CHAR	     = '#'; ///< The 'image' of the border in normal mode
static char MENU_BORDER_CHAR = '*'; ///< The 'image' of the border in the main menu

int high_score;
char *id;
int gametype=1;

void settID();

//Sets the username of the player
void settID(char *user){
	id=user;
}

/*Simple enum to make the colors easier to read: FOREGROUND_BACKGROUND */
enum Colors { BLACK_WHITE = 1, CYAN_BLACK, BLUE_BLACK,
              WHITE_BLACK, GREEN_BLACK, RED_BLACK,  };

struct screen_t screen;
struct game_t game;


/**	Just erases everything to black
 */
void draw_background ()
{
	clear();
}


/* Draws the window border */
void draw_borders ()
{
	int i;

	attron (COLOR_PAIR (WHITE_BLACK));
	for (i = 0; i <= (screen.width-1); i++)	
	  {
	    mvaddch (1, i, BORDER_CHAR);
	    mvaddch ((screen.height-1), i, BORDER_CHAR);
	  }
	for (i = 1; i <= (screen.height-1); i++)	
	  {
	    mvaddch (i, 0, BORDER_CHAR);
	    mvaddch (i, (screen.width-1), BORDER_CHAR);
	  }

}


/* Draws the snake - from the head to the whole body */
void draw_player ()
{
	attron (COLOR_PAIR (GREEN_BLACK));
	mvaddch (tron.body[0].y, tron.body[0].x, PLAYER_HEAD_CHAR);

	int i;
	for (i = 1; i < tron.size; i++)
		mvaddch (tron.body[i].y, tron.body[i].x, PLAYER_CHAR);
}


/* Prints the current score */
void draw_score ()
{
	attron (COLOR_PAIR (WHITE_BLACK));
	mvprintw (0, 0,  "----------------   Level (speed): %2d", game.level);
	mvprintw (0, 36, " ---- Score: %4d ---User: %s---", tron.score, id);
}


/* Exits and dealocates the memory required by ncurses */
void engine_exit ()
{
	clear ();
	refresh ();
	// Effectively ends ncurses mode
	endwin ();
}


/**	Get the user input during game and make the right decisions
 */
void engine_get_game_input ()
{
	// The input variable MUST be int to accept non-ascii characters
	int input = getch ();

	switch (input)
	{

	case ERR:
		// If we get no input
		break;

	case KEY_UP:    
		player_change_direction (UP);
		break;

	case KEY_LEFT:  
		player_change_direction (LEFT);
		break;

	case KEY_DOWN:  
		player_change_direction (DOWN);
		break;

	case KEY_RIGHT: 
		player_change_direction (RIGHT);
		break;

	case 'q':	
	case 'Q':
		engine_exit ();
		ifitron_exit ();
		break;


	default:
		break;
	}
}


/**	Starts the game engine. Initializes all the stuff related to ncurses.
 *
 *  @note If some engine-specific initialization fails, the game aborts.
 */
void engine_init ()
{
	screen.width  = 80;
	screen.height = 24;

	// Starts the ncurses mode
	initscr ();

	if (has_colors() == FALSE)
		ifitron_abort ("Your terminal does not support colors.\n");

	// Start support for colors ( Name, Foreground, Background )
	start_color ();
	init_pair (GREEN_BLACK, COLOR_GREEN, COLOR_BLACK);
	init_pair (CYAN_BLACK,  COLOR_CYAN,  COLOR_BLACK);
	init_pair (WHITE_BLACK, COLOR_WHITE, COLOR_BLACK);
	init_pair (RED_BLACK,   COLOR_RED,   COLOR_BLACK);
	init_pair (BLUE_BLACK,  COLOR_YELLOW,  COLOR_BLACK);
	init_pair (BLACK_WHITE, COLOR_BLACK, COLOR_WHITE);

	int current_height, current_width;
	// Gets the current width and height of the terminal
	getmaxyx (stdscr, current_height, current_width);

	if ((current_width < screen.width) || (current_height < screen.height))
		ifitron_abort ("Your console screen is smaller than 80x24\n"
		               "Please resize your window and try again\n\n");

	// Character input doesnt require the <enter> key anymore
	raw ();

	// Makes the cursor invisible
	curs_set (0);

	// Support for extra keys (life F1, F2, ... )
	keypad (stdscr, true);

	// Wont print the input received
	noecho ();

	// Wont wait for input - the game will run instantaneously
	nodelay (stdscr, true);

	// Refresh the screen (prints whats in the buffer)
	refresh ();
}


/**	Draws the Game Over screen.
 *
 *	Besides drawing 'Game Over', it highlights where the player died.
 */
void engine_show_game_over ()
{
	attron (COLOR_PAIR (RED_BLACK));
	mvaddch (tron.body[0].y, tron.body[0].x, 'x');

	free(tron.body);
	tron.body = NULL;

	mvprintw (10, 17, "GAME OVER");
	mvprintw (12, 17, "Returning to main menu");
	mvprintw (11, 17, "USERNAME: %s SCORE: %d", id, tron.score);
	draw_score ();

	refresh ();
}

//Init for top five leaderboard, not yet used
struct topfive* top_init(size_t size){
	struct topfive *t;
	int i=0;
	while (i<size){
		t[i].score=0;
		t[i].speed=0;
		i++;
	}

	return t;
}

/* Displays the main menu and gets the user input from it.
 *
 * This function blocks the game execution and waits for user input,
 * refreshing the main menu screen according to the options selected. */

void engine_show_main_menu ()
{

	if(id==NULL){
		ifitron_abort("\n Missing username\n");
	}
	int wait = TRUE;

	int speed_option = 1;
	char speed_options[9] = {'1', '2', '3', '4', '5', '6', '7', '8', '9'};

	int menu_x_padding = 18;
	int option_x_padding = menu_x_padding + 17;

	clear ();

	while (wait == TRUE)
	{
		// The borders
		attron (COLOR_PAIR (WHITE_BLACK));
		int i;
		for (i = 0; i < screen.width; i++)
		{
			mvaddch (0, i, MENU_BORDER_CHAR);
			mvaddch (screen.height - 1, i, MENU_BORDER_CHAR);
		}
		for (i = 0; i < screen.height; i++)
		{
			mvaddch (i, 0, MENU_BORDER_CHAR);
			mvaddch (i, screen.width - 1, MENU_BORDER_CHAR);
		}

		attron (COLOR_PAIR (GREEN_BLACK));

		//prints out top five scores if connected to highscoreserver
		if(high_score){
			/*if(top==NULL){
				top_init(5);
			}*/

			mvprintw(1, 40, "+-------------------------------+");
			mvprintw(2, 40, "|   Top 5, scores               |");
			mvprintw(3, 40, "|pos | name | score     |speed  |");
			mvprintw(4, 40, "|1   |      |           |       |");
			mvprintw(5, 40, "|2   |      |           |       |");
			mvprintw(6, 40, "|3   |      |           |       |");
			mvprintw(7, 40, "|4   |      |           |       |");
			mvprintw(8, 40, "|5   |      |           |       |");
			mvprintw(9, 40, "+-------------------------------+");
			mvprintw (7, 12, "Connected to: highscore");

		}
		mvprintw (8, 12,  "IFI-TRON");
		mvprintw (9, 12,  "Username: %s", id);

		attron (COLOR_PAIR (BLUE_BLACK));
		mvprintw (10, 12, "+---------------------------------------------------+");
		mvprintw (11, 12, "|                                                   |");
		mvprintw (12, 12, "|                                                   |");
		mvprintw (13, 12, "|                                                   |");
		mvprintw (14, 12, "|                                                   |");
		mvprintw (15, 12, "|                                                   |");
		mvprintw (16, 12, "+---------------------------------------------------+");
		mvprintw (12, menu_x_padding, "Press <enter> or <space> to start game");
		mvprintw (13, menu_x_padding, "Press <q> to quit game");

		// And here we draw the level numbers
		attron (COLOR_PAIR (BLUE_BLACK));
		mvprintw (15, menu_x_padding, "Starting speed/level:");

		// Tricky, draw the options with the right colors
		int j;
		for (i = 0, j = 0; i < 9; i++)
		{
			if (i == (speed_option-1))
				attron (COLOR_PAIR (WHITE_BLACK));
			else
				attron (COLOR_PAIR (BLUE_BLACK));

			mvprintw (15, option_x_padding+j+6, "%c", speed_options [i]);
			j += 2;
		}

		attron (COLOR_PAIR (WHITE_BLACK));
		mvprintw (screen.height-3, 3, "Use --help for guidelines");

		// Now we wait for orders
		wait = get_main_menu_input (&speed_option);

		// This function is so refreshing...
		refresh ();
	}

	game.level = speed_option;
	gametype=speed_option; //Sets the level for sending over server
}




/**	Completely draws the screen during game.
 *
 * 	The usleep() function "stops" the program for 'n' microseconds.
  */
void engine_show_screen ()
{
	draw_background ();
	draw_borders ();
	draw_player ();
	draw_score ();

	usleep (REFRESH_DELAY);

	refresh();
}


/* Gets input for the main menu */
int get_main_menu_input (int* speed_option)
{
	int input = getch();

	switch (input)
	{
	case ERR: // no input
	        break;

	case '\n': case ' ':
		return FALSE;
		break;

	case 'q': case 'Q':
		engine_exit();
		ifitron_exit();
		break;

	case KEY_LEFT:
		if (*speed_option > 1) (*speed_option)--;
		break;

	case KEY_RIGHT:
		if (*speed_option < 9) (*speed_option)++;
		break;

	case '1':
		*speed_option = 1;
		break;
	case '2':
		*speed_option = 2;
		break;
	case '3':
		*speed_option = 3;
		break;
	case '4':
		*speed_option = 4;
		break;
	case '5':
		*speed_option = 5;
		break;
	case '6':
		*speed_option = 6;
		break;
	case '7':
		*speed_option = 7;
		break;
	case '8':
		*speed_option = 8;
		break;
	case '9':
		*speed_option = 9;
		break;

	default:
		break;
	}

	return TRUE;
}


/* Aborts the game and displays the error message */
void ifitron_abort (char* error_msg)
{
	engine_exit ();
	printf ("%s", error_msg);
	exit (EXIT_FAILURE);
}

/* Interrupts the game and quits to the terminal. */
void ifitron_exit ()
{
	if (tron.body != NULL)
	{
		free (tron.body);
		tron.body = NULL;
	}
	//Sends out a packet over the server
	if(high_score){
		packet p;
		create_pck(0, 10, 0, 0, 0, &p);
		if(send_prepck(high_score, sizeof(p), 0, 1)<0){
			ifitron_abort("\nError on sending pre packet at exit\n");
		}
		if(send_pck(high_score, p)<0){
			ifitron_abort("\nError on sending packet at exit\n");
		}
		/*int error = send_packet(high_score, 0, 0, 10, 0, 0, 0, 0);
		if(error<0){
			ifitron_abort("\nError on sending message at exit\n");
		}*/

		close(high_score);
	}
	if(multiplayer){
		close(multiplayer);
	}
	exit (EXIT_SUCCESS);
}

/* Finish the game after haveing lost a life. */
void ifitron_game_over ()
{
	//Sends a packet over the server
	if(high_score){
		if(!multiplayer){
			uint16_t name_len=strlen(id);
			packet p;
			//char* name=htonl(id);
			create_pck(tron.score, 01, gametype, 0, 0, &p);

			if (send_prepck(high_score, sizeof(p), 0, 1)<0){
				ifitron_abort("\nError on sending pre packet\n");
			}

			if(send_pck(high_score, p)<0){
				ifitron_abort("\nError on sending packet\n");
			}

			//int error = send_packet(high_score, name_len, tron.score, 01, gametype, 16, 0, 0);
			//if(error<0){
			//	ifitron_abort("\nError on sending message\n");
			//}

			/*if(send_name(high_score, name_len, id)<0){
				ifitron_abort("\nError on sending name\n");
			}*/

			if(write(high_score, id, 25)<0){
				ifitron_abort("\nWrong on sending name\n");
			}

		}else if(multiplayer){

		}
	}
	engine_show_game_over ();
	usleep (4000000); // Wait 4 seconds before returning...
	engine_show_main_menu ();
	ifitron_init ();
}


/* Starts all the necessairy stuff */
void ifitron_init ()
{
	player_init ();
	engine_show_screen ();
}

/*
 * Starts highscore server
 * */

void init_highscore(char* addr, int port){

	struct sockaddr_in serveraddr;

	memset(&serveraddr, 0, sizeof(struct sockaddr_in));

	serveraddr.sin_family=AF_INET;
	//serveraddr.sin_addr.s_addr=inet_addr(addr);

	inet_pton(AF_INET, addr, &serveraddr.sin_addr);
	serveraddr.sin_port = htons(port);

	high_score = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);

	if (high_score <0){
		ifitron_abort("\n wrong on socket \n");
	}

	if(connect(high_score, (struct sockaddr*)&serveraddr, sizeof(struct sockaddr_in))<0){
		ifitron_abort("\nWrong on connect\n");
	}

}

/*
 * Starts multiplayer server
 * */

void init_multi(char* addr, int port){

	struct sockaddr_in serveraddr;

	memset(&serveraddr, 0, sizeof(struct sockaddr_in));

	serveraddr.sin_family=AF_INET;
	//serveraddr.sin_addr.s_addr=inet_addr(addr);

	inet_pton(AF_INET, addr, &serveraddr.sin_addr);
	serveraddr.sin_port = htons(port);

	multiplayer = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);

	if (multiplayer <0){
		ifitron_abort("\n wrong on socket \n");
	}

	if(connect(multiplayer, (struct sockaddr*)&serveraddr, sizeof(struct sockaddr_in))<0){
		ifitron_abort("\nWrong on connect\n");
	}


}
