/*-------------------------------------
 funciones.c
-------------------------------------*/

#include "defines.h"
#include "obsprites.h"
#include "funciones.h"
#include "sprites.h"

// Muestra en consola el tiempo de juego y la puntuacion
void msgTiempoScore(){
	iprintf("\x1b[06;00H  + Tiempo: %3d segundos     +  ", seg);
	iprintf("\x1b[08;00H  + Puntuacion: %3d puntos   +  ", score);
	iprintf("\x1b[12;00H  + Saltos disponibles: %2d  +  ", nSaltos);
}

// Muestra en consola informacion al terminar la partida
void msgPartidaTerminada(){
	iprintf("\x1b[08;00H  +    PARTIDA FINALIZADA    +  ");
	iprintf("\x1b[10;00H PUNTUACION FINAL: %3d puntos   ", score);
	iprintf("\x1b[12;00H TIEMPO DE PARTIDA:%3d segundos ", TIEMPO_PARTIDA-seg);
	iprintf("\x1b[20;00H  <START>  - Nueva partida      ");
	iprintf("\x1b[22;00H  <SELECT> - Salir              ");
	switch(motivoGO){
		case GO_TIME:
			iprintf("\x1b[14;00H MOTIVO FIN: Tiempo agotado     ");
			break;
		case GO_CAPTURA:
			iprintf("\x1b[14;00H MOTIVO FIN: Pacman capturado   ");
			break;
		case GO_TECLA:
			iprintf("\x1b[14;00H MOTIVO FIN: Tecla <A> pulsada  ");
			break;
	}
}

// Muestra en consola instrucciones durante la partida
void msgInstrucciones(){
	iprintf("\x1b[16;00H  <DPAD>   - Mover Pacman       ");
	iprintf("\x1b[18;00H  <B>      - Saltar             ");
	iprintf("\x1b[20;00H  <START>  - Pausar             ");
	iprintf("\x1b[22;00H  <A>      - Salir              ");
}

// Muestra en consola mensaje de partida pausada
void msgPause(){
	iprintf("\x1b[10;00H  +     PARTIDA  PAUSADA     +  ");
	iprintf("\x1b[20;00H  <START> - Reanudar            ");
}

// Muestra en consola el menu con "facil" seleccionado
void msgFacil(){
	iprintf("\x1b[08;00H  +Selecciona una dificultad:+  ");
	iprintf("\x1b[10;00H  +\033[32mFACIL\033[37m  INTERMEDIO  DIFICIL+  ");
	iprintf("\x1b[16;00H  Pulsa <TACTIL> para iniciar ");
}

// Muestra en consola el menu con "intermedio" seleccionado
void msgMedio(){
	iprintf("\x1b[08;00H  +Selecciona una dificultad:+  ");
	iprintf("\x1b[10;00H  +FACIL  \033[32mINTERMEDIO\033[37m  DIFICIL+  ");
	iprintf("\x1b[16;00H  Pulsa <TACTIL> para iniciar ");
}

// Muestra en consola el menu con "dificil" seleccionado
void msgDificil(){
	iprintf("\x1b[08;00H  +Selecciona una dificultad:+  ");
	iprintf("\x1b[10;00H  +FACIL  INTERMEDIO  \033[32mDIFICIL\033[37m+  ");
	iprintf("\x1b[16;00H  Pulsa <TACTIL> para iniciar ");
}

// Mensaje al cerrar el juego
void msgFin(){
	iprintf("\x1b[12;00H  +    SALIENDO DEL JUEGO    +  ");
}

// Limpia la consola
void msgClean(){
	consoleClear();
	iprintf("\x1b[02;00H  +--------------------------+  ");
	iprintf("\x1b[03;00H  : EC 18/19           G33   :  ");
	iprintf("\x1b[04;00H  +--------------------------+  ");
}

// Reinicia las estadisticas al iniciar nueva partida
void reiniciarStats(){
	seg = TIEMPO_PARTIDA + 1;
	score = 0;
	ticks = TICK_SEGUNDO - 1;
	tickMove1 = 0;
	tickMove2 = 0;
	tickMove3 = 0;
	tickCD = 0;
	delaySalto = 0;
	nSaltos = NSALTOS;
}

// Funcion para limpiar todos los sprites de la pantalla inferior
void limpiarSubPantalla(){
	BorrarMsPacman(miPacman.xSprit, miPacman.ySprit);
	BorrarFantasma(fantasma1.xSprit, fantasma1.ySprit);
	BorrarFantasma2(fantasma2.xSprit, fantasma2.ySprit);
	BorrarFantasma3(fantasma3.xSprit, fantasma3.ySprit);
	BorrarPacdot(OAM_PUNTO1, punto1.xSprit, punto1.ySprit);
	BorrarPacdot(OAM_PUNTO2, punto2.xSprit, punto2.ySprit);
	BorrarPacdot(OAM_PUNTO3, punto3.xSprit, punto3.ySprit);
	BorrarPacdot(OAM_PUNTO4, punto4.xSprit, punto4.ySprit);
	BorrarPacdot(OAM_PUNTO5, punto5.xSprit, punto5.ySprit);
	BorrarCereza(OAM_CEREZA1, cereza1.xSprit, cereza1.ySprit);
	BorrarCereza(OAM_CEREZA2, cereza2.xSprit, cereza2.ySprit);
	BorrarMierda(OAM_MIERDA1, mierda1.xSprit, mierda1.ySprit);
	BorrarMierda(OAM_MIERDA2, mierda2.xSprit, mierda2.ySprit);
}

// Funcion para al terminar la partida pasemos al estado "STOP", guardamos el motivo de la finalizacion y limpiamos las 2 pantallas
void partidaTerminada(int motivo){
	estado = ST_STOP;
	motivoGO = motivo;
	msgClean();
	limpiarSubPantalla();
}

// Funcion para inicializar los sprites segun el nivel de dificultad
void inicializarSprites(int dif){
	// Pacman inicializamos siempre igual
	inicializarSprit(&miPacman);
	MostrarMsPacman(miPacman.xSprit, miPacman.ySprit);
	switch(dif){
		case FACIL:
			// un fantasma1, 5 puntos y 2 cerezas
			inicializarSprit(&fantasma1);
			inicializarSprit(&punto1);
			inicializarSprit(&punto2);
			inicializarSprit(&punto3);
			inicializarSprit(&punto4);
			inicializarSprit(&punto5);
			inicializarSprit(&cereza1);
			inicializarSprit(&cereza2);
			
			MostrarFantasma(fantasma1.xSprit, fantasma1.ySprit);
			MostrarPacdot(OAM_PUNTO1, punto1.xSprit, punto1.ySprit);
			MostrarPacdot(OAM_PUNTO2, punto2.xSprit, punto2.ySprit);
			MostrarPacdot(OAM_PUNTO3, punto3.xSprit, punto3.ySprit);
			MostrarPacdot(OAM_PUNTO4, punto4.xSprit, punto4.ySprit);
			MostrarPacdot(OAM_PUNTO5, punto5.xSprit, punto5.ySprit);
			MostrarCereza(OAM_CEREZA1, cereza1.xSprit, cereza1.ySprit);
			MostrarCereza(OAM_CEREZA2, cereza2.xSprit, cereza2.ySprit);
			break;
		case INTERMEDIO:
			// un fantasma1 y un fantasma3, 3 puntos, una cereza y una mierda
			inicializarSprit(&fantasma1);
			inicializarSprit(&fantasma3);
			inicializarSprit(&punto1);
			inicializarSprit(&punto2);
			inicializarSprit(&punto3);
			inicializarSprit(&cereza1);
			inicializarSprit(&mierda1);

			MostrarFantasma(fantasma1.xSprit, fantasma1.ySprit);
			MostrarFantasma3(fantasma3.xSprit, fantasma3.ySprit);
			MostrarPacdot(OAM_PUNTO1, punto1.xSprit, punto1.ySprit);
			MostrarPacdot(OAM_PUNTO2, punto2.xSprit, punto2.ySprit);
			MostrarPacdot(OAM_PUNTO3, punto3.xSprit, punto3.ySprit);
			MostrarCereza(OAM_CEREZA1, cereza1.xSprit, cereza1.ySprit);
			MostrarMierda(OAM_MIERDA1, mierda1.xSprit, mierda1.ySprit);
			break;
		case DIFICIL:
			// un fantasma2 y un fantasma3, 2 puntos, una cereza y 2 mierdas
			inicializarSprit(&fantasma2);
			inicializarSprit(&fantasma3);
			inicializarSprit(&punto1);
			inicializarSprit(&punto2);
			inicializarSprit(&cereza1);
			inicializarSprit(&mierda1);
			inicializarSprit(&mierda2);

			MostrarFantasma2(fantasma2.xSprit, fantasma2.ySprit);
			MostrarFantasma3(fantasma3.xSprit, fantasma3.ySprit);
			MostrarPacdot(OAM_PUNTO1, punto1.xSprit, punto1.ySprit);
			MostrarPacdot(OAM_PUNTO2, punto2.xSprit, punto2.ySprit);
			MostrarCereza(OAM_CEREZA1, cereza1.xSprit, cereza1.ySprit);
			MostrarMierda(OAM_MIERDA1, mierda1.xSprit, mierda1.ySprit);
			MostrarMierda(OAM_MIERDA2, mierda2.xSprit, mierda2.ySprit);
			break;
		default:
			break;
	}//switch
}

// Pacman realiza un salto hacia la direccion que se mueve
void saltoPacman(struct Sprit *sp){
	int i;
	if (nSaltos > 0 && delaySalto == TICK_SEGUNDO){
		for (i=0; i<PX_PACMAN; i++){
			moverSprit(sp);
		}
		nSaltos = nSaltos - 1;
		delaySalto = 0;
	}
}

// El fantasma realiza un salto hacia la direccion de pacman, cada x segundos (x = int tiempo)
int saltoFantasma(struct Sprit *sp, int tickCD, int tiempo){
	int i;
	if (tickCD == (tiempo*TICK_SEGUNDO)){
		for (i=0; i<PX_FAN; i++){
			moverSprit(sp);
			perseguirPac(sp, &miPacman);
		}
		return 0;
	} else {
		return tickCD;
	}
	
}

// Realiza el movimiento del pacman completo (mover, mostrar)
int movimientoPacman(struct Sprit *pac, int tickMove, int frecuencia){
	if (tickMove == frecuencia){
		moverSprit(pac);
		MostrarMsPacman(pac->xSprit, pac->ySprit);
		return 0;
	} else {
		return tickMove;
	}
}

// Realiza el movimiento del fantasma completo (perseguir, mover, mostrar)
int movimientoFantasma(struct Sprit *fan, int tickMove, int frecuencia, int nFan){
	if (tickMove == frecuencia && seg < TIEMPO_PARTIDA){
		perseguirPac(fan, &miPacman);
		moverSprit(fan);
		
		if(nFan == 1){
			MostrarFantasma(fan->xSprit, fan->ySprit);
		} else if(nFan == 2){
			MostrarFantasma2(fan->xSprit, fan->ySprit);
		} else {
			MostrarFantasma3(fan->xSprit, fan->ySprit);
		}
		return 0;
	} else {
		return tickMove;
	}
}

// Funciones para comprobar si pacman ha comido puntos, cerezas o mierdas
void comerPunto(struct Sprit *p, int index){
	if(capturado(&miPacman, p)){
		score = score + 1;
		inicializarSprit(p);
		MostrarPacdot(index, p->xSprit, p->ySprit);
	}
}
void comerCereza(struct Sprit *c, int index){
	if(capturado(&miPacman, c)){
		score = score + 3;
		nSaltos = nSaltos + 1;
		inicializarSprit(c);
		MostrarCereza(index, c->xSprit, c->ySprit);
	}
}
void comerMierda(struct Sprit *m, int index){
	if(capturado(&miPacman, m)){
		score = score - 2;
		inicializarSprit(m);
		MostrarMierda(index, m->xSprit, m->ySprit);
	}
}
