/*-------------------------------------
 funciones.h
-------------------------------------*/
// Diferentes funciones que usaremos

// Funciones de la consola
extern void msgTiempoScore();
extern void msgPartidaTerminada();
extern void msgInstrucciones();
extern void msgPause();
extern void msgFacil();
extern void msgMedio();
extern void msgDificil();
extern void msgFin();
extern void msgClean();

// Reiniciar variables globales
extern void reiniciarStats();
extern void limpiarSubPantalla();
extern void partidaTerminada(int motivo);

// Funciones de los sprites
extern void inicializarSprites(int dif);
extern void saltoPacman(struct Sprit *sp);
extern int saltoFantasma(struct Sprit *sp, int tickCD, int tiempo);
extern int movimientoPacman(struct Sprit *pac, int tickMove, int frecuencia);
extern int movimientoFantasma(struct Sprit *fan, int tickMove, int frecuencia, int nFan);
extern void comerPunto(struct Sprit *p, int index);
extern void comerCereza(struct Sprit *c, int index);
extern void comerMierda(struct Sprit *m, int index);
