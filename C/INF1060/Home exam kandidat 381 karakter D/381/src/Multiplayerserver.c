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

int port;

void terminate_server(char* msg){
	printf("%s", msg);
	exit(1);

}

void signal_callback_handler(int signum){

	terminate_server("\nServer turned of\n");
}

void args_handler(int argc, char* argv[]){
	int i=0;
	while (i<argc){
		if(strcmp(argv[i], "-p") == 0 || strcmp(argv[i], "--port")==0){
			port=atoi(argv[i+1]);
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


				}
			}
		}

	}
	close(request_sd);
	//close(sd);
}
