/*-------------------------------------
 teclado.c
-------------------------------------*/

// Anadir los includes que sean necesarios
#include <nds.h>
#include <stdio.h>
#include "defines.h"
#include "sprites.h"
#include "obsprites.h"
#include "funciones.h"

// Esta funcion tiene que devolver el valor de la tecla pulsada
int  TeclaPulsada() {
   // Devuelve el valor asociado a la tecla pulsada: 
   // A=0; B=1; SELECT=2; START=3; DCHA=4; IZDA=5;
   // ARRIBA=6; ABAJO=7; R=8; L=9;
   // -1 en otros casos
	if (!(TECLAS_DAT & 0x0001)) return A;
	if (!(TECLAS_DAT & 0x0002)) return B;
	if (!(TECLAS_DAT & 0x0004)) return SELECT;
	if (!(TECLAS_DAT & 0x0008)) return START;
	if (!(TECLAS_DAT & 0x0010)) return DCHA;
	if (!(TECLAS_DAT & 0x0020)) return IZDA;
	if (!(TECLAS_DAT & 0x0040)) return ARRIBA;
	if (!(TECLAS_DAT & 0x0080)) return ABAJO;
	if (!(TECLAS_DAT & 0x0100)) return R;
	if (!(TECLAS_DAT & 0x0200)) return L;
	return -1;
} // TeclaPulsada()


// Rutina de atencion a la interrupcion del teclado
void IntTec() {
	int tecla = TeclaPulsada();
	switch(estado){
		case ST_WAIT:
			switch(tecla){
				// Intercambiamos el nivel de dificultad con los botones <L> y <R>
				case L:
					if(dificultad == DIFICIL){
						dificultad = INTERMEDIO;
						msgMedio();
					} else {
						dificultad = FACIL;
						msgFacil();
					}
					break;
				case R:
					if(dificultad == FACIL){
						dificultad = INTERMEDIO;
						msgMedio();
					} else {
						dificultad = DIFICIL;
						msgDificil();
					}
					break;
				default:
					break;
			}//switch
			// Delay 1000ms
			swiDelay(4190000);
			break;
		case ST_MOVE:
			switch(tecla){
				case B:
					saltoPacman(&miPacman);
					break;
				case A:
					partidaTerminada(GO_TECLA);
					break;
				case START:
					estado = ST_PAUSE;
					msgClean();
					break;
				case IZDA:
					cambiarDireccion(&miPacman, DIR_LEFT);
					GirarPacman(DIR_LEFT);
					break;
				case ARRIBA:
					cambiarDireccion(&miPacman, DIR_UP);
					GirarPacman(DIR_UP);
					break;
				default:
					break;
			}//switch
			break;
		case ST_PAUSE:
			if(tecla == START){
				msgClean();
				estado = ST_MOVE;
			}
			break;
		case ST_STOP:
			if(tecla == START){
				msgClean();
				msgFacil();
				estado = ST_WAIT;
			}
			break;
		default:
			break;
	}//switch
	tecla = -1;
}
