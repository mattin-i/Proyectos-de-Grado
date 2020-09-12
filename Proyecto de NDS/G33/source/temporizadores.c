/*-------------------------------------
 temporizadores.c
-------------------------------------*/
// Anadir los includes que sean necesarios
#include "defines.h"
#include "sprites.h"
#include <nds.h>
#include <stdio.h>
#include "obsprites.h"
#include "funciones.h"

// Rutina de atencion a la interrupcion del temporizador
void IntTemp() {
	switch(estado){
		case ST_MOVE:
			// Ticks cases
			ticks = ticks + 1;
			tickMove1 = tickMove1 + 1;
			if(seg < TIEMPO_PARTIDA){
				tickMove2 = tickMove2 + 1;
				tickMove3 = tickMove3 + 1;
				tickCD = tickCD + 1;
			}
			if(delaySalto < TICK_SEGUNDO){
				delaySalto = delaySalto + 1;
			}
			
			// Tiempo de juego
			if (ticks == TICK_SEGUNDO) {
				ticks = 0;
				seg = seg - 1;
				msgTiempoScore();
			}
			
			// Mueve pacman
			tickMove1 = movimientoPacman(&miPacman, tickMove1, FREC_PACMAN);

			// Controla el movimiento cada fantasma segun el nivel de dificultad
			// Comprueba si pacman ha capturado algun pacdot, cereza o mierda
			switch(dificultad){
				case FACIL:
					// Mueve fantasma1
					tickMove2 = movimientoFantasma(&fantasma1, tickMove2, FREC_FAN1, 1);
					// Comprueba si fantasma ha capturado a pacman
					if (capturado(&fantasma1, &miPacman)){
						partidaTerminada(GO_CAPTURA);
					}

					// Comprueba si pacman ha comido algun item
					if (tickMove1 == 0){
						comerPunto(&punto1, OAM_PUNTO1);
						comerPunto(&punto2, OAM_PUNTO2);
						comerPunto(&punto3, OAM_PUNTO3);
						comerPunto(&punto4, OAM_PUNTO4);
						comerPunto(&punto5, OAM_PUNTO5);
						comerCereza(&cereza1, OAM_CEREZA1);
						comerCereza(&cereza2, OAM_CEREZA2);
					}
					break;
				case INTERMEDIO:
					// Mueve fantasma1 y fantasma3
					tickMove2 = movimientoFantasma(&fantasma1, tickMove2, FREC_FAN1, 1);
					tickMove3 = movimientoFantasma(&fantasma3, tickMove3, FREC_FAN3, 3);
					tickCD = saltoFantasma(&fantasma3, tickCD, TIEMPO_SALTO1);
					// Comprueba si fantasma ha capturado a pacman
					if (capturado(&fantasma1, &miPacman) || capturado(&fantasma3, &miPacman)){
						partidaTerminada(GO_CAPTURA);
					}

					// Comprueba si pacman ha comido algun item
					if (tickMove1 == 0){
						comerPunto(&punto1, OAM_PUNTO1);
						comerPunto(&punto2, OAM_PUNTO2);
						comerPunto(&punto3, OAM_PUNTO3);
						comerCereza(&cereza1, OAM_CEREZA1);
						comerMierda(&mierda1, OAM_MIERDA1);
					}
					break;
				case DIFICIL:
					// Mueve fantasmas2 y fantasma3
					tickMove2 = movimientoFantasma(&fantasma2, tickMove2, FREC_FAN2, 2);
					tickMove3 = movimientoFantasma(&fantasma3, tickMove3, FREC_FAN3, 3);
					tickCD = saltoFantasma(&fantasma3, tickCD, TIEMPO_SALTO2);
					// Comprueba si fantasma ha capturado a pacman
					if (capturado(&fantasma2, &miPacman) || capturado(&fantasma3, &miPacman)){
						partidaTerminada(GO_CAPTURA);
					}

					// Comprueba si pacman ha comido algun item
					if (tickMove1 == 0){
						comerPunto(&punto1, OAM_PUNTO1);
						comerPunto(&punto2, OAM_PUNTO2);
						comerCereza(&cereza1, OAM_CEREZA1);
						comerMierda(&mierda1, OAM_MIERDA1);
						comerMierda(&mierda2, OAM_MIERDA2);
					}
					break;
				default:
					break;
			}
			
			// Partida terminada por tiempo
			if(seg == 0){
				partidaTerminada(GO_TIME);
			}
			break;
		default:
			break;
	}//switch
} // IntTemp()
