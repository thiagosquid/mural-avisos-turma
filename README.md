# Mural de Avisos
## Este é um projeto desenvolvido para um sistema de avisos, forum de dúvidas e eventos para minha turma da faculdade.

###### Inicialmente a pretensão era de desenvolver algo limitado apenas à nossa turma, ADS 2021.1. <br>Mas logo se viu a possibilidade de extender para outras turmas então precisou-se pensar em definir Super-usuários, Admins e Usuários comuns.
:tv: O front-end desta aplicação está sendo desenvolvido em React por <a src="https://github.com/arthursaldanha">Arthur Saldanha</a>. O código pode ser encontrado neste <a src="https://github.com/arthursaldanha/Mural-Turma">Repositório</a>.

### Pontos desenvolvidos até agora:
- [x] Cadastro limitado apenas às pessoas que possuem o email acadêmico da instituição (@academico.ifs.edu.br)
- [x] Validação de cadastro através de envio de email
- [x] Funcionalidade de troca de senha com envio de token por email
- [x] Fornecimento de token e refresh token para autorização de acesso a endpoints da API

### A desenvolver
- [ ] Endpoint para que algum Super-usuário promova um usuário comum a Admin de turma, e este autorize um usuário comum a ter acesso à sua turma
- [ ] Endpoint de realização de publicação
- [ ] Endpoint para comentários em postagem
- [ ] Endpoint para realização de pesquisa por palavras chaves
