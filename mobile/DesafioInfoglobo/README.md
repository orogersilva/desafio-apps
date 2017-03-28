[![Build Status](https://www.bitrise.io/app/d2313a699a87b338.svg?token=mxXviIT8SxmXMUZpsNOy2A)](https://www.bitrise.io/app/d2313a699a87b338)

# Desafio Infoglobo

Link para o Android app: https://play.google.com/apps/testing/com.orogersilva.desafioinfoglobo

<div>
    <div class="imgContainer">
        <img src="/screenshots/infoglobo_1.jpg"/>
    </div>
    <div class="imgContainer">
        <img src="/screenshots/infoglobo_2.jpg"/>
    </div>
</div>

# Observações

* Todos os requisitos do app foram cumpridos.
* Para a primeira execução do app, é um requisito que o dispositivo do usuário esteja conectado à internet. Em vezes seguintes, não é necessário, pois o funcionamento do app é "designing for offline".
* "Clean Architecture" foi a arquitetura escolhida para projetar o app.
* Foram implementados testes unitários para a camada de acesso à Web API. Testes unitários para os presenters, camada de "domain" e view (via Robolectric) podem ser implementados com facilidade, uma vez que as camadas estão dividindo bem suas responsabilidades e estão com baixo acoplamento.
* Em certos casos, views aparecem em tom cinza sem preenchimento de qualquer imagem. A view poderia ter sida omitida e ter sido feito um redesign do layout para comportar somente o texto da notícia.
* Algumas notícias não contém texto. Como alternativa, na tela de detalhes da notícia, eu embarcaria uma webview e carregaria a notícia (com seu texto) direto da URL original.

* Mais sobre mim

Github: https://github.com/orogersilva (contém dezenas de projetos de apps produzidos por mim)
Meu livro: https://www.casadocodigo.com.br/products/livro-entrega-continua-android
Linkedin: https://www.linkedin.com/in/orogersilva/
Twitter: https://twitter.com/orogersilva
E-mail: orogersilva@gmail.com
