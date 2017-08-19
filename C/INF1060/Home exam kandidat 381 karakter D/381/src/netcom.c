#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <ncurses.h>
#include <unistd.h>
#include <netinet/in.h>
#include <sys/socket.h>

#include "netcom.h"


struct packet pack;

/*
 * Sets packet's values to some that does not disturb highscore testing.
 */
void reset_pck(packet *p){
	if(p==NULL){
		return;
	}else{
		/*
		p->score=0;
		p->name_len=0;
		p->size=0;*/
		p->msg_type=4;
		//free(p.user);
		/*p->gameType=0;
		p->antComp=0;
		p->antApon=0;
		p->reserv=0;
		free(p->user);*/
		return;
	}
}

void reset_prepck(prepacket *pp){
	if(pp==NULL){
		return;
	}else{
		pp->msg_type=4;
	}
}

/*
 * Sets value of a packet to send over the network.
 * */
void create_pck(uint16_t score, uint8_t msg_type, uint8_t gametype, uint8_t antComp, uint8_t antApon, packet *p){

	p->score=htons(score);
	//p->user=id;
	p->msg_type=msg_type;
	p->gameType=gametype;
	p->antComp=antComp;
	p->antApon=antApon;

}
/*
 * Early and successful attempt to send information, but not used due to sending of size in prepacket.
 * */
int send_packet(int sd, uint8_t name_len, uint16_t score, uint8_t msg_type, uint8_t lvl, uint16_t size, uint8_t comp, uint8_t ap){
	packet pck;

	pck.size=htons(size);
	pck.msg_type=msg_type;
	pck.gameType=lvl;
	pck.score=htons(score);
	pck.antComp=htons(comp);
	pck.antApon=htons(ap);
	pck.name_len=name_len;

	//pck.name_len=htonl(strlen(id));
	//write(sd, id, pck.name_len);

	if(write(sd, &pck, sizeof(struct packet))<0){
		return -1;
	}else{
		return 1;
	}
}

/*
 * Sends packet over socket
 *
 * return value:
 * -1 if unsuccessful
 * 1 if successful
 * */
int send_pck(int sd, packet p){

	if(write(sd, &p, sizeof(p))<0){
		return -1;
	}else{
		return 1;
	}
}

/*
 * Sends a prepacket with info on the next packets. "Poengliste"
 *
 * return values:
 * -1 if unsuccessful
 * 1 if successful
 * */
int send_prepck(int sd, uint16_t size, uint8_t msg_t, uint8_t n){
	prepacket pp;
	pp.antPck=n;
	pp.msg_type=msg_t;
	pp.size=size;

	if(write(sd, &pp, sizeof(prepacket))<0){
		return -1;
	}else{
		return 1;
	}
}


/*
 * Receives packet from socket
 *
 * return values:
 * -1 if unsuccessful
 * 1 if successful
 * */
int rec_pre(int sd, prepacket* pp){
	prepacket tmp;

	if(read(sd, &tmp, sizeof(prepacket))<0){
		return -1;
	}else{
		pp->antPck=tmp.antPck;
		pp->msg_type=tmp.msg_type;
		pp->size=tmp.size;

		return 1;
	}

}

/*
 * Early and successful attempt to recive a packet from socket and sets values
 *
 * return values:
 * -1 if unsuccessful
 * 1 if successful
 * */
int recive_packet(int sd, packet* pck){
	packet tmp;
	if(read(sd, &tmp, sizeof(packet))<0){
		return -1;
	}
	else{
		pck->score=ntohs(tmp.score);
		pck->size=ntohs(tmp.size);
		pck->msg_type=tmp.msg_type;
		pck->gameType=tmp.gameType;
		pck->antComp=ntohs(tmp.antComp);
		pck->antApon=ntohs(tmp.antApon);
		pck->name_len=tmp.name_len;
		//pck->user=ntohl(tmp.user);

		return 1;
	}
}

/*
 * Early attempt to send username
 * */
int send_name(int sd, uint8_t name_len, char* id){

	char *msg;
	memcpy(msg, id, (name_len*sizeof(char)));
	if(write(sd, msg, (name_len*sizeof(char)))<0){
		return -1;
	}else{
		return 1;
	}
	return 0;
}


/*
 * Sends buffer for multiplayer
 * */
int send_buffer(){
	return 0;
}

/*
 * Recieves buffer for Multiplayer
 * */
int recive_buffer(){
	return 0;
}
