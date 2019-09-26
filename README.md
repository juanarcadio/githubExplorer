# githubExplorer

Workaround del reto **6 grados de mierda** [Episodio 51 de PEUM](https://www.programaresunamierda.com/2019/09/episodio-51-kubernetes-y-rancher.html).
Cuyo objetivo es conectar los repositorios de loowid y de linux.

Nos ayudaremos de la api de github, para obtener los repos de un usuario: https://api.github.com/users/loowid/repos?type=owner

Y para cada repo las relaciones con otros usuarios:
- Los usuarios que han hecho forks: https://api.github.com/repos/loowid/loowid/forks?sort=stargazers (forks by)
- El usuario del que hicimos fork: https://api.github.com/repos/loowid/connect-mongo/forks?sort=stargazers (forked from)

Con estas herramientas, vamos a montar una estructura conceptualmente de árbol, donde los nodos son los usuarios y las aristas el repositorio que une a esos dos usuarios.
Realizaremos un recorrido de búsqueda primero en anchura, desde Loowid hasta llegar a Torvalds... Para mejorar el rendimiento de la búsqueda, la vamos a realizar en paralelo, es decir, desde Loowid hasta Torvalds y desde Torvalds hasta Loowid, para ello relizaremos la citada búsqueda en anchura, buscando un nodo al que llamaremos Nexo, que pertenezca a los dos árboles.

Y, sorprendentemente, solo hay un tio en entre loowid y linux, nuestro nexo de unión es [modulexcite] https://github.com/modulexcite

loowid works in (loowid/loowid) forks by modulexcite
torvalds works in (torvalds/linux) forks by modulexcite

Subo el código, aunque está hecho unos zorros, entre las prisas y las agonías, pero ahí va.
