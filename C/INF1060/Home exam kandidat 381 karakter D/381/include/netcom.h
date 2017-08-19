#ifndef NETCOM_H_
#define NETCOM_H_

#define TRUE 1
#define FALSE 0

#define NEW_SCORE
#define CLOSE_SCORE

typedef struct packet{
	uint16_t score;
	uint16_t name_len;
	uint16_t size;
	uint8_t msg_type;
	uint8_t gameType;
	uint8_t antComp;
	uint8_t antApon;
	int reserv;
	char *user;
}packet;

typedef struct prepacket{
	uint16_t size;
	uint8_t msg_type;
	uint8_t antPck;
}prepacket;

struct topfive{
	uint16_t score;
	uint8_t speed;
	char* user;
};

void create_packet(int sd, uint16_t score, uint8_t msg_type, uint8_t lvl, uint16_t size, uint8_t comp, uint8_t ap, packet *pck);
int send_prepck(int sd, uint16_t size, uint8_t msg_t, uint8_t n);
int rec_pre(int sd, prepacket* pp);
int send_pck(int sd, packet p);

int send_msg(int sd, uint16_t score, uint8_t msg_type, uint8_t level, uint16_t size, uint8_t comp, uint8_t ap);
//void send_msg(int sd, char* msg);
int rec_msg(int sd, packet *p, size_t n);

void create_pck(uint16_t score, uint8_t msg_type, uint8_t gametype, uint8_t antComp, uint8_t antApon, packet *p);
int send_packet(int sd, uint8_t name_len, uint16_t score, uint8_t msg_type, uint8_t lvl, uint16_t size, uint8_t comp, uint8_t ap);
struct packet reci_packet(int sd);

int recive_packet(int sd, packet *pck);
void reset_pck(packet *p);
int send_name(int sd, uint8_t name_len, char* id);

#endif
