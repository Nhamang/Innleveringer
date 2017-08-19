
#ifndef HIGHSCORESERVER_H_
#define HIGHSCORESERVER_H_


struct board{
	uint16_t score;
	size_t name_len;
	size_t size;
	uint8_t gameType;
	uint8_t msg_type;
	uint8_t antComp;
	uint8_t antApon;
	uint8_t boardsize;
	int reserv;
	char *user;
};


struct board* single_board_init(size_t size);
struct board* multi_board_init(size_t size);

int get_pos();
int add_score_single(packet pck);

#endif
