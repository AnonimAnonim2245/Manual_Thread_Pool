# Descriere implementare
Tema 1 APD - Luca Plian 332CA

## Voi descrie prima data, implementarea threadpool-ului.
Prima data, am incercat sa creez threadpoolul propriul zis, am luat o structura ConcurrencyQueue pentru task-uri
pentru a le rula in paralel in fiecare thread, iar coada este comuna pentru toate thredurile.
Si folosim concurrency pentru a nu avea nicio problema intre threaduri

Noi adaugam de fiecare data in coada taskurile, care vor fi folosite de fiecare thread.
Inainte de a fi folosite noi creeam o listaComuna cu ele cu o functie custom de thread(care mosteneste threadurile), si le pornim pe fiecare
si ele vor rula in background pana cand le vom opri.

Asa, am adaugat taskurile in coada, iar fiecare dintre threaduri(nu stim neaparat care) vor apuca sa preia un element din taskul si il prelucreze.
Pana cand ori elementele adaugate pentru readers-writers este goala(la inceput vom avea nevoie ca sa ramanem in loop) sau daca
inca coada taskurilor nu este goala inca. Astea sunt conditiile pentru care ramanem in loop.

Astfel noi in while, in doTask, vom avea un runnable(Runnable ne permite ca interfata sa rulam o executam pe un anumit thread.

## Problema cititori-scriitori, implementari
Acum hai sa vorbim despre implementari, pe prima am luat-o de pe mobylabs, iar celelalte de pe https://www.cs.kent.edu/~farrell/osf03/oldnotes/L15.pdf,
pentru a doua varianta am intampinat cele mai multe dificultati, ramaneam blocat si aparea fenomenul de starvation si asa am observat
ca waiting_readurile ramaneau si nu se mai luau, asa ca am pus un while, am incercat si pentru waiting writers, dar am vazut dupa ca apare
problema de racing condition nu este okay.

## Dificultati intampinate de catre mine
La mine initial timpul dura foarte mult, am zis sa umblu la semafoare sa le fac capacitatea mai mare, dar imi dadea cu eroare ceea ce era logic.
M-am gandit dupa ca indexii intre ei nu comunica, asa ca m-am decis sa fac un sharedvalue(cu semafoare si locks) pentru fiecare index ca sa scutesc din timp ca nu prea impacta,
si un sharedvalue global ca sa iau lista mea si sa o transmit mai departe
si am facut pentru toate cele trei locuri asa.

Si ca sa nu se termine programul mai repede decat trebuie, l-am pus pe program sa stea un pic mai mult in bucla while pana cand ori avem toata lista cu taskuri si incep sa se termine dintre ele.

Imi merg toate testele in test, cu exceptia 17 uneori, uneori imi trec toate.
