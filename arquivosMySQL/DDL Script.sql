-- SELECT nome, v_total, qtd, dia  FROM ((CLIENTE INNER JOIN pedido_produto) INNER JOIN ITEM_PRODUTO) ORDER BY nome;
-- SELECT * FROM FUNCIONARIO WHERE CPF = '3371611' LIMIT 1;
CREATE TABLE ALMOXARIFADO(
id integer(7), 
descr varchar(100) not null,
primary key (id));
CREATE TABLE ESTABELECIMENTO (
codigo integer(7), 
CNPJ char(14) not null, 
nome_fantasia varchar(30) not null, 
cidade varchar(30) not null, 
email varchar(50) not null, 
CEP char(8) not null, 
logradouro varchar(50) not null, 
telefone integer(11) not null, 
hora_fecha time not null, 
hora_abertura time not null, 
id_almox integer(7) not null,
emergencia24h boolean,
tipo_est enum('Petshop', 'Clínica') not null,
primary key (codigo),
foreign key (id_almox) references ALMOXARIFADO(id) ON DELETE CASCADE);
CREATE TABLE CLIENTE(
CPF char(12), 
email varchar(40)  not null, 
dt_nasc date  not null, 
cidade varchar(30)  not null, 
nome varchar(50) not null, 
CEP char(8) not null, 
Logradouro varchar(70) not null,
dt_ultimo_login date, 
cartão_cred integer(16), 
senha varchar(15) not null,
confirmado boolean default false,
primary key (CPF));
CREATE TABLE TELEFONES(
cpf_cl char(12), 
fone bigint(11) not null,
primary key (cpf_cl, fone),
foreign key (cpf_cl) references CLIENTE (CPF));
CREATE TABLE FUNCIONARIO(
CPF char(11), 
email varchar(40)  not null, 
dt_nasc date  not null, 
cidade varchar(30)  not null, 
Nome varchar(50) not null, 
CEP char(8) not null, 
Logradouro varchar(70) not null,
dt_admissao date not null, 
cod_est integer(7) not null, 
senha varchar(15) not null,
CRMV varchar(6) unique,
tipo_fun enum('Tecnico', 'Veterinario') not null,
primary key (CPF),
foreign key(cod_est) references ESTABELECIMENTO(Codigo));
CREATE TABLE HORARIO(
id integer(7) AUTO_INCREMENT, 
dia date not null, 
hr_inicio time not null, 
hr_fim time not null, 
periodo enum('matutino', 'vespertino', 'noturno', 'plantão') not null,
primary key (id));
CREATE TABLE ESPECIALIDADE(
id integer(7) AUTO_INCREMENT, 
descricao varchar(60) not null,
primary key (id));
CREATE TABLE RAÇA(
cod integer(7) AUTO_INCREMENT, 
porte_animal enum('pequeno porte', 'medio porte', 'grande porte') not null, 
descr varchar(70) not null, 
obs varchar(60),
primary key (cod));
CREATE TABLE ANIMAL(
id integer(7) AUTO_INCREMENT, 
nomea varchar(20) not null, 
dt_nasc date not null,
idade integer(2) not null, 
cpf_cliente char(12) not null, 
cod_raça integer(7) not null,
primary key (id),
foreign key(cpf_cliente) references CLIENTE(CPF) ON DELETE CASCADE,
foreign key(cod_raça) references RAÇA (cod));
CREATE TABLE SUBSTANCIA(
cod integer(7) AUTO_INCREMENT,  
descr varchar(70) not null,
primary key (cod));
CREATE TABLE FATURA(
cod bigint(10) auto_increment, 
stats enum('pagamento confirmado', 'aguardando confirmação de pagamento', 'expirado') not null, 
vl_total numeric(6,2) not null, 
dt_venc date not null, 
primary key(cod),
check (vl_total>0));
CREATE TABLE PEDIDO_PRODUTO(
cod varchar(10), 
cod_fat bigint(10),
dia date not null, 
v_total numeric(6,2) not null, 
stats enum('confirmado', 'não finalizado', 'erro ao processar pedido') not null, 
desconto numeric(6,2), 
cpf_cliente char(12) not null,
primary key (cod),
check (v_total >0),
foreign key(cod_fat) references FATURA(cod) ON DELETE CASCADE,
foreign key(cpf_cliente) references CLIENTE(CPF));
CREATE TABLE AGENDAMENTO(
id int(16) AUTO_INCREMENT, 
tipo_agendamento enum('PET', 'CLI', 'LAB') not null, 
confirmado boolean default false, 
foi_efetivado boolean default false, 
foi_cancelado boolean default false, 
hora_fim time not null, 
hora_inicio time not null, 
dt_agenda date not null, 
cod_fatura bigint(10) not null,
primary key (id),
foreign key(cod_fatura) references FATURA(cod) ON DELETE CASCADE);
CREATE TABLE TIPO_SERVICO(
cod integer(7) AUTO_INCREMENT, 
categoria enum('cirurgico', 'exame', 'estético') not null, 
descr varchar(70) not null,
primary key (cod));
CREATE TABLE SERVICO(
cod integer(7) AUTO_INCREMENT, 
obs varchar(70), 
preco numeric(6,2)  not null, 
dt_solicitacao date not null, 
dt_realizacao date not null, 
duracao integer(2) not null, 
cod_tipo integer(7) not null,
primary key (cod),
check (preco > 0),
foreign key(cod_tipo) references TIPO_SERVICO(cod));
CREATE TABLE SERVICO_PETSHOP(
cod integer(7),
primary key (cod),
foreign key(cod) references SERVICO(cod) ON DELETE CASCADE);
CREATE TABLE SERVICO_CLINICA(
cod integer(7), 
motivo varchar(70) not null, 
resultado varchar(70) not null default 'Não Divulgado',
primary key (cod),
foreign key(cod) references SERVICO(cod) ON DELETE CASCADE);
CREATE TABLE LABORATORIAL(
cod integer(7), 
nome_lab varchar(30) not null, 
fone_lab integer(11) not null,
primary key (cod),
foreign key(cod) references SERVICO(cod) ON DELETE CASCADE);
CREATE TABLE DIAGNOSTICO(
id varchar(10), 
obs varchar(70), 
descrição varchar(100) not null, 
grau_urgencia enum('baixo', 'normal', 'grave', 'gravíssimo') not null, 
cod_lab integer(7) not null,
primary key (id),
foreign key(cod_lab) references LABORATORIAL(cod) ON DELETE CASCADE);
CREATE TABLE DELIVERY(
id varchar(10), 
horario time not null, 
dia date not null, 
stats enum('entregue', 'a caminho', 'aguardando entregador') not null, 
endereco varchar(100) not null, 
cod_pedido varchar(10) not null,
primary key(id),
foreign key(cod_pedido) references PEDIDO_PRODUTO(cod) ON DELETE CASCADE);
CREATE TABLE FORMA_PAG(
cod integer(2) AUTO_INCREMENT, 
descr varchar(30) not null,
primary key (cod));
CREATE TABLE PAGAMENTO(
id int(16) AUTO_INCREMENT, 
vl_total numeric(6,2) not null, 
dt_pag date not null, 
vl_multa numeric(6,2), 
cod_fat bigint(10) not null, 
cod_forma integer(2) not null,
primary key(id),
check (vl_total>0),
foreign key(cod_fat) references FATURA(cod) ON DELETE CASCADE,
foreign key(cod_forma) references FORMA_PAG(cod));
CREATE TABLE CATEGORIA(
cod integer(7) AUTO_INCREMENT, 
descr varchar(50) not null,
primary key (cod));
CREATE TABLE INSTANCIA(
seq varchar(10), 
cod_barra integer(13) not null, 
dt_fabricacao date, 
dt_validade date, 
qtd_atual integer(4) not null, 
id_almox integer(7) not null,
primary key (seq),
foreign key(id_almox) references ALMOXARIFADO(id));
CREATE TABLE PRODUTO_REF(
cod integer(7), 
foto longblob not null, 
descrição varchar(100) not null, 
fabricante varchar(30) not null, 
vl_unitario numeric(6,2) not null, 
tipo_unidade varchar(20) not null, 
qtd_minima integer(2) not null, 
cod_cat integer(7) not null,
seq_inst varchar(10),
primary key (cod),
check (vl_unitario > 0),
foreign key(cod_cat) references CATEGORIA(cod),
foreign key(seq_inst) references INSTANCIA(seq) ON DELETE CASCADE);
CREATE TABLE ITEM_PRODUTO(
cod_pedido varchar(10), 
cod_produto integer(7), 
seq varchar(10), 
qtd integer(4) not null, 
valor_parcial numeric(6,2) not null,
primary key(cod_pedido, cod_produto, seq),
check (valor_parcial>0),
foreign key(cod_pedido) references PEDIDO_PRODUTO(cod) ON DELETE CASCADE,
foreign key(cod_produto) references PRODUTO_REF(cod) ON DELETE CASCADE);
CREATE TABLE REALIZA(
cpf_cliente char(12), 
cpf_func char(11), 
id_agend int(16) AUTO_INCREMENT, 
data_marcada date not null,
primary key (cpf_cliente, cpf_func, id_agend),
foreign key(cpf_cliente) references CLIENTE(CPF),
foreign key(cpf_func) references FUNCIONARIO(CPF),
foreign key(id_agend) references AGENDAMENTO(id) ON DELETE CASCADE);
CREATE TABLE POSSUI_HOR(
CPF_func char(11), 
id_horario integer(7),
primary key (CPF_func, id_horario),
foreign key(CPF_func) references FUNCIONARIO(CPF),
foreign key(id_horario) references HORARIO(id));
CREATE TABLE TEM_ESP(
CPF_vet char(11), 
id_esp integer(7),
primary key (CPF_vet, id_esp),
foreign key(CPF_vet) references FUNCIONARIO(CPF),
foreign key(id_esp) references ESPECIALIDADE(id));
CREATE TABLE ALERGICO(
id_animal integer(7), 
cod_substancia integer(7),
primary key (id_animal, cod_substancia),
foreign key(id_animal) references ANIMAL(id),
foreign key(cod_substancia) references SUBSTANCIA(cod));
CREATE TABLE ENVOLVE(
id_animal integer(7),
id_agend int(16) AUTO_INCREMENT,
primary key (id_animal, id_agend),
foreign key(id_animal) references ANIMAL(id),
foreign key (id_agend) references AGENDAMENTO(id) ON DELETE CASCADE);
CREATE TABLE ENVOLVE_AP(
id_agendpet int(16) AUTO_INCREMENT, 
cod_servpet integer(7),
primary key (id_agendpet, cod_servpet),
foreign key(id_agendpet) references AGENDAMENTO(id) ON DELETE CASCADE,
foreign key(cod_servpet) references SERVICO_PETSHOP(cod));
CREATE TABLE ENVOLVE_AC(
id_agendclin int(16) AUTO_INCREMENT, 
cod_servclin integer(7),
primary key (id_agendclin, cod_servclin),
foreign key(id_agendclin) references AGENDAMENTO(id) ON DELETE CASCADE,
foreign key(cod_servclin) references SERVICO_CLINICA(cod));
CREATE TABLE ENVOLVE_AL(
id_agendlab int(16) AUTO_INCREMENT,
cod_servlab integer(7),
primary key (id_agendlab, cod_servlab),
foreign key(id_agendlab) references AGENDAMENTO(id) ON DELETE CASCADE,
foreign key(cod_servlab) references LABORATORIAL (cod));
CREATE TABLE FORNECE_CL(
cod_clin integer(7), 
cod_servc integer(7),
primary key (cod_clin, cod_servc),
foreign key(cod_clin) references ESTABELECIMENTO(codigo),
foreign key(cod_servc) references SERVICO_CLINICA(cod));
CREATE TABLE FORNECE_PET(
cod_pet integer(7), 
cod_servp integer(7),
primary key (cod_pet, cod_servp),
foreign key(cod_pet) references ESTABELECIMENTO(codigo),
foreign key(cod_servp) references SERVICO_PETSHOP(cod));