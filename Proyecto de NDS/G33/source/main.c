/*---------------------------------------------------
Este codigo se ha implementado basandose en el ejemplo "Simple sprite demo" de 
dovoto y otro de Jaeden Amero
---------------------------------------------------*/

#include <nds.h>
#include <stdio.h>
#include <stdlib.h>	    // srand, rand,...
#include <unistd.h>
#include <time.h>       // time 

#include "fondos.h"
#include "sprites.h"
#include "defines.h"
#include "rutserv.h"
#include "teclado.h"
#include "temporizadores.h"
#include "obsprites.h"
#include "funciones.h"

//---------------------------------------------------
// Funciones
//---------------------------------------------------

/******************** TactilTocada() *********************/
// Esta funcion consulta si se ha tocado la pantalla tactil
int TactilTocada() {
	touchPosition pos_pantalla;
	touchRead(&pos_pantalla);
	return !(pos_pantalla.px==0 && pos_pantalla.py==0);
} 

//---------------------------------------------------
// Variables globales
//---------------------------------------------------

int estado; // Estado en el que se encuentra el programa
int dificultad; // Dificultad de la partida
int motivoGO; // Motivo por el que la partida termina
int seg; // Tiempo restante de partida
int score; // La puntuacion total de la partida
int ticks; // Tick para los segundos
int tickMove1; // Tick para movimiento pacman
int tickMove2; // Tick para movimiento fantasma
int tickMove3; // Tick para movimiento fantasma2
int tickCD; // Tick para el tiempo que fantasma tiene para saltar
int delaySalto; // Delay para el salto de pacman
int nSaltos; // Numero de saltos disponibles para pacman

//---------------------------------------------------
// main
//---------------------------------------------------
int main() {

	/* Poner en marcha el motor grafico 2D. */
	powerOn(POWER_ALL_2D);

	/* Establecer la pantalla inferior como principal, inicializar el sistema grafico
		y configurar el sistema de fondos. */
	lcdMainOnBottom();
	initVideo();
	initFondos();

	/* Mostrar fondos en pantalla. */
	SetFondo();
	//mostrarFondoSub();

	/* Inicializar memoria de sprites y guardar en ella los sprites */
	initSpriteMem();
	guardarSpritesEnMemoria();

	/* Establecer las paletas para los sprites */
	establecerPaletaPrincipal();
	establecerPaletaSecundaria();

	/* Inicializa la consola (superior) de texto. 
		Nota.- Para borrar la pantalla existe la funcion consoleClear() */
	consoleDemoInit();	

	/* Para inicializar el generador de numeros aleatorios en funcion de una semilla */
	srand (time(NULL));

	/* Limpiamos la consloa e incluimos la siguiente cabecera para que cada grupo la modifique con
		su numero de grupo "xx" en "Gxx". */
	msgClean();
	msgFacil();

	interrupciones();
	estado = ST_WAIT;
	dificultad = FACIL;
	int tecla;
	
	while(estado != ST_OFF) {
		// Respuesta teclado
		tecla = TeclaPulsada();
		switch(estado){
			case ST_WAIT:
				// Cambiar de estado al tocar la pantalla tactil
				if(TactilTocada()){
					estado = ST_RUN;
				}
				break;
			case ST_RUN:
				// Inicializamos los sprites y cambiamos de estado
				inicializarSprites(dificultad);
				msgClean();
				reiniciarStats();
				estado = ST_MOVE;
				break;
			case ST_MOVE:
				msgInstrucciones();
				switch(tecla){
					case DCHA:
						cambiarDireccion(&miPacman, DIR_RIGHT);
						GirarPacman(DIR_RIGHT);
						break;
					case ABAJO:
						cambiarDireccion(&miPacman, DIR_DOWN);
						GirarPacman(DIR_DOWN);
						break;
					default:
						break;
				}//switch
				break;
			case ST_PAUSE:
				msgPause();
				break;
			case ST_STOP:
				msgPartidaTerminada();
				if(tecla == SELECT){
					// Con select se cierra el juego
					estado = ST_OFF;
					msgClean();
					msgFin();
				}
			default:
				break;
		}//switch
		tecla = -1;
		swiWaitForVBlank();
	}//while
	exit(0);
} // main
