CC=gcc
GAMEFILES = src/player.c src/engine.c src/netcom.c src/ifitron.c
SCOREFILES = src/highscoreserver.c src/netcom.c
MULTIPLAYERFILES= src/Multiplayerserver.c src/netcom.c
LIBS= -lncurses
INCLUDE = -Iinclude


all: game highscore multiplayer

game:
	$(CC) $(INCLUDE) $(GAMEFILES) $(LIBS) -o ifitron

highscore:
	$(CC) $(INCLUDE) $(SCOREFILES) -o high

multiplayer:
	$(CC) $(INCLUDE) $(MULTIPLAYERFILES) -o multi
clean:
	rm high ifitron multi