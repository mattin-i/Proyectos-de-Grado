/*-------------------------------------
sprites.h
-------------------------------------*/

u16* gfxMsPacman;
u16* gfxPacdot;
u16* gfxFantasma;
u16* gfxFantasma2;
u16* gfxFantasma3;
u16* gfxCereza;
u16* gfxMierda;

/* Inicializar la memoria de Sprites. */
extern void initSpriteMem();

/* Dentro de esta funcion hay que definir el color con el que se mostrara cada uno de los 256 
 * colores posibles en la pantalla principal. El 0 es transparente y los no definidos son negros.
 */
extern void establecerPaletaPrincipal();

/* Dentro de esta funcion hay que definir el color con el que se mostrara cada uno de los 256 
 * colores posibles en la pantalla secundaria. El 0 es transparente y los no definidos son negros.
 */
extern void establecerPaletaSecundaria();

/* Para guardar los sprites en memoria y luego poder usarlos. */

extern void guardarSpritesEnMemoria();

/* 
 * Funciones especificas para manejar los sprites. 
 */
extern void GirarPacman(int angle);
extern void MostrarMsPacman(int x, int y);
extern void BorrarMsPacman(int x, int y);

extern void MostrarFantasma (int x, int y);
extern void BorrarFantasma(int x, int y);
extern void MostrarFantasma2 (int x, int y);
extern void BorrarFantasma2(int x, int y);
extern void MostrarFantasma3 (int x, int y);
extern void BorrarFantasma3(int x, int y);

extern void MostrarPacdot (int indice, int x, int y);
extern void BorrarPacdot(int indice, int x, int y);
extern void MostrarCereza (int indice, int x, int y);
extern void BorrarCereza(int indice, int x, int y);
extern void MostrarMierda (int indice, int x, int y);
extern void BorrarMierda(int indice, int x, int y);
