#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <time.h>
#include <netdb.h>
#include <signal.h>
#include <getopt.h>

#include "netcom.h"
#include "highscoreserver.h"

FILE *save;
char *saved;
char name[25];

int port;
struct board *single;
struct board *multi;


/*
 * Turnes of the server with given message
 * */
void terminate_server(char* msg){
	printf("%s", msg);
	exit(1);

}

/*
 * Turns of server on error
 * */
void error(const char *msg){
	perror(msg);
	exit(1);
}

//Start of Score handeling part of the server

/*
 * Creates new Single scoreboard.
 *
 * */
struct board* single_board_init(size_t size){

	struct board* s_b;
	//s_b = malloc(sizeof(struct single_board));
	s_b = malloc(sizeof(struct board)*size+(25*sizeof(char)));
	int memsize = size*sizeof(struct board);
	int t=0;
	while (t<size){
		s_b[t].score=0;
		s_b[t].boardsize=size;
		t++;
	}
	return s_b;
}

/*
 * Creates new multiplayer scoreboard
 * */
struct board* multi_board_init(size_t size){

	struct board* m_b;
	//s_b = malloc(sizeof(struct single_board));
	m_b = malloc(sizeof(struct board)*size);
	int memsize = size*sizeof(struct board);
	int t=0;
	while (t<size){
		m_b[t].score=0;
		m_b[t].boardsize=size;
		t++;
	}
	return m_b;
}

/*
 *Prints out all single scores that ar over 0 points
 * */
void print_single(){
	int t=0;
	printf("\nUsernames:\t\tScores:\t\tSpeed:\t\tStats:");
	printf("\n");
	char *name;
	while (t<25){
		if(single[t].score>0){
			printf("%d: %s\t\t", t+1, single[t].user);
			printf("%d\t\t", single[t].score);
			printf("%d\t\t", single[t].gameType);
			printf("%d\t%d\n", single[t].antApon, single[t].antComp);

		}
		t++;
	}
}

/*
 * Reads scoretype and sends packet to correct leaderbord
 * */
void handle_score(packet p){
	//printf("\nBefore handling \n");
	if((p.gameType==1) || (p.gameType==2)|| (p.gameType==3)|| (p.gameType==4)|| (p.gameType==5)|| (p.gameType==6)|| (p.gameType==7)|| (p.gameType==8)|| (p.gameType==9)){
		//printf("\nInn score handling\n");
		add_score_single(p);
	}else if((p.gameType==11) || (p.gameType==12) || (p.gameType==13) || (p.gameType==14) || (p.gameType==15) || (p.gameType==16) || (p.gameType==17) || (p.gameType==18) || (p.gameType==19)){
		add_score_multiplayer(p);
	}
}

/*
 * Loads scores from given filename
 * */
void load_scores(char *savefile){

	char ch[80];
	char *token;
	printf("%s", savefile);
	save=fopen(savefile, "r");
	if(!save){
		terminate_server("\nMissing file\n");
	}
	packet p;
	uint16_t score;
	uint8_t gametype;
	char *tmp;
	char *tmp2;
	char *tmp3;
	char *tmp4;
	uint8_t tmp5;

	while((fgets(ch, 80, save)) !=NULL){
		printf("\n%s\n", ch);
		int i=0;
		//printf("\nprint 2: %s\n", strtok(NULL,";"));
		//printf("\nprint 3: %s\n", strtok(NULL,";"));

		p.user=strtok(ch,";");
		tmp=strtok(NULL, ";");
		tmp2=strtok(NULL, ";");
		tmp3=strtok(NULL, ";");
		tmp4=strtok(NULL, ";");
		p.score=atoi(tmp);
		p.gameType=atoi(tmp2);
		p.antComp=atoi(tmp3);
		p.antApon=atoi(tmp4);

		if(p.antComp>0 || p.antApon>0){
			tmp2=tmp2+10;
			p.gameType=tmp5;
			handle_score(p);
		}else{
			handle_score(p);
		}
	}

}

/*
 * adds score to multiplayer leaderboard,
 * */
int add_score_multiplayer(packet pck){
	int t=0;
		struct board tmp;
		struct board s;

		s.score=pck.score;
		s.size=pck.size;
		s.gameType=pck.gameType;
		s.antComp=pck.antComp;
		s.antApon=pck.antApon;
		//strcpy(s.user, pck.user);
		s.reserv=pck.reserv;
		s.user=pck.user;
		s.msg_type=pck.msg_type;
		//printf(s.user);

		printf("\nScore: %d\n", s.score);

		char *name1;
		char *name2;
		if(multi ==NULL){
			multi=multi_board_init(25);
		}
		int min=24;
		//printf("\n%d\n", min);
		if(pck.score > multi[min].score){
			while (t<min+1){
				if (pck.score > multi[t].score){
					strcpy(name1, single[t].user);
					strcpy(name2, s.user);
					tmp=multi[t];
					multi[t]=s;
					s=tmp;
					strcpy(s.user, name1);
					strcpy(single[t].user, name2);
				}
				t++;
			}

		}
		return 0;
}

/*
 * Add a new score to fitting position
 *
 * */
int add_score_single(packet pck){
	int t=0;
	struct board tmp;
	struct board s;
	packet pcktmp;
	char *name;
	char *name2;

	//printf("\nCheckpoint 1\n");
	pcktmp=pck;

	s.score=pck.score;
	s.size=pck.size;
	s.gameType=pck.gameType;
	s.antComp=pck.antComp;
	s.antApon=pck.antApon;
	s.reserv=pck.reserv;
	s.user=pck.user;
	//printf("\n%s", s.user);

	//printf("\nCheckpoint 2\n");
	//printf("\nScore: %d\n", s.score);
	//printf("\nCheckpoint 3\n");

	if(single ==NULL){
		//printf("\nBefor init\n");
		single=single_board_init(25);
	}
	//printf("\nAfter init\n");
	int n=0; //Used to find position where te score is added
	int min=24;
	int pos=0;

	if(pck.score > single[min].score){//Checks if the score is more the the smallest in the leaderboard
		//printf("\nIn first if\n");
		while (t<min+1){
			//printf("\nIn while loop\n");
			if (pck.score > single[t].score){
				//printf("\nIn second if\n");
				name=single[t].user;
				name2=s.user;
				tmp=single[t];
				single[t]=s;
				s=tmp;
				//s.user=name;
				//single[t].user=name2;
				n++;
			}
			t++;
		}
		pos=26-n;
		printf("\nAdded %s with score of %d on position: %d\n", pcktmp.user, pcktmp.score, pos);

	}
	return 0;
}


/*
 * Saves score to predetermined filename.
 * */
void save_scores(){
	int n=25;
	int t=0;
	save=fopen("saved.sav", "a+");
	//fprintf(save, "start single\n");
	while (t<n){
		if(single[t].score>0){
			int error = fprintf(save, "%s;%d;%d;%d;%d\n", single[t].user, single[t].score, single[t].gameType, single[t].antComp, single[t].antApon);
			if(error<0){
			printf("\nUnable to print\n");
			}
		}
		t++;
	}
	//fprintf(save, "end single\n");

	fclose(save);
	n=0;
}

//End of score handling part of server

/*
 * Catches ctrl-c and terminates server
 * */
void signal_callback_handler(int signum){
	
	printf("\nCaught signal\n");

	printf("\nHIGHSCORE\n");
	if(single==NULL){
		printf("\nNo scores to save\n");
	}else{
		print_single();
		save_scores();
		single=NULL;
	}
	terminate_server("\nServer turned of\n");
}

/*
 * Prints out packet for checking if it arrives correct
 * */
void print_score(packet p){
	printf("\nScores\n");
	printf("%d\n", p.score);

	printf("msg_type\n");
	printf("%d\n", p.msg_type);

	printf("Size\n");
	printf("%d\n", p.size);

	printf("game_type\n");
	printf("%d\n", p.gameType);

	printf("antApon\n");
	printf("%d\n", p.antApon);

	printf("antComp\n");
	printf("%d\n", p.antComp);

	printf("Name_len\n");
	printf("%d\n", p.name_len);

	printf("Name\n");
	printf("%s\n", p.user);

}



/*
 * Handles input paramater's
 * */
void args_handler(int argc, char* argv[]){
	int i=0;
	while (i<argc){
		if(strcmp(argv[i], "-p") == 0 || strcmp(argv[i], "--port")==0){
			port=atoi(argv[i+1]);
		}
		else if((strcmp(argv[i], "-load"))==0 || (strcmp(argv[i], "-l"))==0){
			if(argv[i+1]){
				saved=argv[i+1];
				load_scores(saved);
			}
			else printf("\nMissing filename\n");
		}
		i++;
	}

}

/*
 * Main part of server.
 * */

int main(int argc, char* argv[]){


	if(argc <1) terminate_server("\n Missing parameters \n");
	if (argc > 1) args_handler (argc, argv);


	signal(SIGINT, &signal_callback_handler);


	int request_sd, sd[2], numsocks, maxsocks;
	int clientaddrlen, i, rc;
	struct sockaddr_in serveraddr;
	struct sockaddr_in clientaddr;

	fd_set fds, readfds;
	struct timeval timeout;

	numsocks=0;

	request_sd = socket(PF_INET, SOCK_STREAM, IPPROTO_TCP);
	if(request_sd <0){
		error("\n ERROR opening socket");
	}

	memset(&serveraddr, 0, sizeof(struct sockaddr_in));

	serveraddr.sin_family = AF_INET;

	serveraddr.sin_addr.s_addr=INADDR_ANY;

	serveraddr.sin_port=htons(port);

	if(bind(request_sd, (struct sockaddr*)&serveraddr, sizeof(struct sockaddr_in))<0){
		error("\n error on binding");
	}

	if(listen(request_sd, SOMAXCONN)<0){
		error("\nFeil på listen\n");
	}

	FD_ZERO(&fds);
	FD_SET(request_sd, &fds);
	clientaddrlen=sizeof(struct sockaddr_in);

	printf("\nListening to port: %d\n", port);



	for(;;){
		int new_sd;
		readfds=fds;
		rc=select(FD_SETSIZE, &readfds, NULL, NULL, &timeout);

		if(rc<0){
			terminate_server("\nError on select\n");
			return -1;
		}

		for(i=0; i<FD_SETSIZE; i++){
			if(FD_ISSET (i, &readfds)){
				if(i==request_sd){
					new_sd=accept(request_sd, (struct sockaddr *)&clientaddr, (socklen_t *)&clientaddrlen);
					FD_SET(new_sd, &fds);
				}else{

					packet p;
					prepacket pp;

					if(rec_pre(i, &pp)<0){
						close(i);
						FD_CLR(i, &fds);
					}

					if(pp.msg_type==0){
						int t=0;
						while(t<pp.antPck){

							if(recive_packet(i, &p)<0){
								close(i);
								FD_CLR(i, &fds);
							}

							if(p.msg_type==1){
								if(read(i, name, 25)<0){
									perror("\nWrong on reciving name\n");
								}else{
									name[24]='\0';
									//printf("\nName: %s\n", name);
									p.user=name;
								}
								handle_score(p);

							}else if(p.msg_type==10){
								close(i);
								FD_CLR(i, &fds);
							}

							t++;
						}

					}

					/*char msg;

					packet p;
					int error = recive_packet(i, &p);
					if(error < 0){
						close(i);
						FD_CLR(i, &fds);
					}

					if(p.msg_type==1){
						if(read(i, name, 25)<0){
							perror("\nWrong on rec name\n");
						}else{
							name[24]='\0';
							printf("\nName: %s\n", name);
							printf("\nAfter printing name\n");
							p.user=name;
						}

						print_score(p);
						handle_score(p);
					}
					else if(p.msg_type==10){
						close(i);
						FD_CLR(i, &fds);
					}*/
					reset_pck(&p);
					reset_prepck(&pp);
				}
			}
		}

	}
	close(request_sd);
	//close(sd);
}
