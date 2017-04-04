
DROP PROCEDURE IF EXISTS updateprod;
DELIMITER //
CREATE PROCEDURE updateprod(
	IN produtocod INT(7),
    IN quantremov INT(4)
)
BEGIN
	SELECT qtd_atual FROM instancia INNER JOIN produto_ref ON instancia.seq=produto_ref.seq_inst WHERE 
    produto_ref.cod=produtocod LIMIT 1 INTO @qtdprod;
    SELECT seq FROM instancia INNER JOIN produto_ref ON instancia.seq=produto_ref.seq_inst WHERE 
    produto_ref.cod=produtocod LIMIT 1 INTO @seqinst;
    SET @novoquant = @qtdprod - quantremov;
    IF(@novoquant >= 0)
    THEN
		UPDATE carepet.instancia SET qtd_atual=@novoquant WHERE seq=@seqinst LIMIT 1;
        ELSE
        SIGNAL SQLSTATE VALUE '45000' SET MESSAGE_TEXT = 
           'Não há quantidade suficiente deste produto no almoxarifado';
	END IF;
END //

-- CALL updateprod(49559788,23);

DROP FUNCTION IF EXISTS fupdateprod;
DELIMITER //
CREATE FUNCTION fupdateprod(
	produtocod INT(7),
    quantremov INT(4)
) returns INT(1)
BEGIN
	SELECT qtd_atual FROM instancia INNER JOIN produto_ref ON instancia.seq=produto_ref.seq_inst WHERE 
    produto_ref.cod=produtocod LIMIT 1 INTO @qtdprod;
    SELECT seq FROM instancia INNER JOIN produto_ref ON instancia.seq=produto_ref.seq_inst WHERE 
    produto_ref.cod=produtocod LIMIT 1 INTO @seqinst;
    SET @novoquant = @qtdprod - quantremov;
    IF(@novoquant >= 0)
    THEN
		UPDATE carepet.instancia SET qtd_atual=@novoquant WHERE seq=@seqinst LIMIT 1;
        RETURN 1;
	
      ELSE
        SIGNAL SQLSTATE VALUE '45000' SET MESSAGE_TEXT = 
           'Não há quantidade suficiente deste produto no almoxarifado';
           END IF;
    RETURN 0;
END //

DROP PROCEDURE IF EXISTS agendamentoCLI;
DELIMITER //
CREATE PROCEDURE agendamentoCLI(
IN nomeanimal VARCHAR(20),IN cpfcli CHAR(12),IN cpffunc CHAR(12), IN tipocod INT(7),IN diamarcado DATE, 
IN horainicio TIME, IN horafim TIME, IN motivocli VARCHAR(70), IN servobs VARCHAR(70)
)
BEGIN
	SELECT precoinicial FROM TIPO_SERVICO WHERE tipo_servico.cod=tipocod LIMIT 1 INTO @preco;
	INSERT INTO fatura(stats,vl_total,dt_venc) VALUES (
    2,
    @preco,
    DATE_ADD(NOW(),INTERVAL 7 DAY));
    SELECT cod FROM fatura ORDER BY cod DESC LIMIT 1 INTO @codfatura;
    INSERT INTO agendamento(tipo_agendamento, hora_inicio, hora_fim, dt_agenda, cod_fatura) VALUES(
    'CLI',
    horainicio,
    horafim,
    diamarcado,
    @codfatura
    );
    INSERT INTO servico(obs,preco,dt_solicitacao,dt_realizacao,duracao,cod_tipo) VALUES(
    servobs,
    @preco,
    NOW(),
    diamarcado,
    TIMESTAMPDIFF(HOUR, horainicio, horafim),
    tipocod
    );
    SELECT cod FROM servico ORDER BY cod DESC LIMIT 1 INTO @codserv;
    INSERT INTO servico_clinica(cod,motivo) VALUES(
    @codserv,
    motivocli
    );
    SELECT id FROM agendamento ORDER BY id DESC LIMIT 1 INTO @idagend;
    INSERT INTO realiza(cpf_cliente,cpf_func,id_agend, data_marcada) VALUES (
    cpfcli,
    cpffunc,
    @idagend,
    diamarcado    
    );    
    SELECT ida FROM animal WHERE cpf_cliente=cpfcli AND nomea=nomeanimal LIMIT 1 INTO @idanimal;
    INSERT INTO envolve(id_animal,id_agend) VALUES (
    @idanimal,
    @idagend
    );    
    INSERT INTO envolve_AC(id_agendclin,cod_servclin) VALUES (
    @idagend,
    @codserv
    );
    
END //



    
DROP TRIGGER IF EXISTS ins_func;
DELIMITER //
CREATE TRIGGER ins_func BEFORE INSERT ON horario
	FOR EACH ROW 
    BEGIN
		IF (EXISTS(SELECT 1 FROM horario WHERE dia_semana = NEW.dia_semana AND hr_inicio = NEW.hr_inicio))
		THEN
           SIGNAL SQLSTATE VALUE '45000' SET MESSAGE_TEXT = 
           'Horário novo não pode ser adicionado pois faz conflito com dia da semana e 
           hora de início de outro horário';
      END IF ;
	END //

	-- INSERT INTO horario(hr_inicio, hr_fim, periodo, dia_semana) VALUES (
   --  '00:00:01', '05:15:00', 'plantão',3
   --  );
    
DROP PROCEDURE IF EXISTS faturames;
DROP PROCEDURE IF EXISTS existUser;
DROP FUNCTION IF EXISTS fgrantq;
DROP FUNCTION IF EXISTS fgetcpf;
DROP PROCEDURE IF EXISTS grantFunc;
DROP PROCEDURE IF EXISTS grantCli;

DELIMITER //
CREATE PROCEDURE faturames(IN mesano DATE,IN clicpf CHAR(12), OUT valorpagar NUMERIC(6,2), OUT diapagar DATE)
BEGIN
	DECLARE valor numeric(6,2) DEFAULT 0;
    DECLARE dataultiag DATE;
	DECLARE finished INTEGER DEFAULT 0;
	DECLARE cur_agenfat CURSOR FOR SELECT dt_agenda, vl_total
    FROM (fatura JOIN agendamento ON cod=cod_fatura) JOIN 
    (envolve JOIN (SELECT * FROM animal WHERE cpf_cliente=clicpf) AS animaiscli ON id_animal=ida)
    ON id=id_agend WHERE 
    MONTH(dt_agenda)=MONTH(mesano) AND YEAR(dt_agenda)=YEAR(mesano) AND confirmado=true
    AND foi_faturado=false AND foi_cancelado=false ORDER BY dt_agenda;
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET finished = 1;
    OPEN cur_agenfat;
    SET valorpagar=0;
    SET diapagar = '2000-01-01';
    
    get_fatura: LOOP
		FETCH cur_agenfat INTO dataultiag,valor;
        IF finished=1 THEN
			LEAVE get_fatura;
        END IF;
        SET valorpagar = valorpagar + valor;
        SET diapagar = DATE_ADD(dataultiag,INTERVAL 7 DAY);    
    END LOOP get_fatura;
    
    IF(valorpagar!=0 AND diapagar!='2000-01-01') THEN
    INSERT INTO fatura(stats,vl_total,dt_venc) VALUES ('aguardando confirmação de pagamento',valorpagar,diapagar);
    END IF;
    CLOSE cur_agenfat;
END //

CALL faturames('2017-04-01','130642302',@valorteste, @diateste);
SELECT @valorteste, @diateste;

-- SELECT *
--     FROM (fatura JOIN agendamento ON fatura.cod=agendamento.cod_fatura) JOIN 
--     (envolve JOIN (SELECT * FROM animal WHERE animal.cpf_cliente='39534217') AS animaiscli 
--     ON envolve.id_animal=ida)
--     ON agendamento.id=envolve.id_agend WHERE 
--      confirmado=true AND foi_faturado=false AND foi_cancelado=false ORDER BY dt_agenda;



DELIMITER //
CREATE PROCEDURE existUser (IN logincpf CHAR(12), OUT sim INT(1))
BEGIN
	IF (SELECT EXISTS(SELECT 1 FROM mysql.user WHERE user = logincpf)=1)
    THEN 
		SET sim = 1;
    else
		SET sim = 0;
	END IF;
END //



DELIMITER //
CREATE FUNCTION fgrantq(grantss VARCHAR(70), tablename VARCHAR(40), usuario VARCHAR(12)) RETURNS VARCHAR(250)
	BEGIN
		SET @fquery=CONCAT('GRANT ',grantss,' ON TABLE ',tablename,' TO ''',usuario,'''@''localhost'';');
        RETURN @fquery;
    END//
    
DELIMITER //
CREATE FUNCTION fgetproduto(codprod INT(7)) RETURNS VARCHAR(200)
BEGIN
	SELECT qtd_atual FROM (produto_ref INNER JOIN instancia ON seq=seq_inst) INNER JOIN almoxarifado
    ON id=id_almox WHERE cod=codprod LIMIT 1 INTO @qtd;
    SELECT descr FROM (produto_ref INNER JOIN instancia ON seq=seq_inst) INNER JOIN almoxarifado
    ON id=id_almox WHERE cod=codprod LIMIT 1 INTO @descr;
    RETURN CONCAT('Quantidade: ',@qtd,' Almoxarifado: ',@descr);
END //

   DELIMITER //
    CREATE FUNCTION fgetcpf() RETURNS CHAR(12)
		BEGIN
			RETURN @cpfcli;
        END //

DROP PROCEDURE IF EXISTS relatoriocli;
DELIMITER //
CREATE PROCEDURE relatoriocli (IN cpfcli CHAR(12))
	BEGIN
    SET @cpfcli=cpfcli;
    DROP VIEW IF EXISTS vrelatoriocli;
    CREATE VIEW vrelatoriocli AS SELECT 
    CPF, email, dt_nasc, cidade, nome, CEP, Logradouro, dt_ultimo_login, confirmadoc, nomea, dt_nasca, idade,
    agendamento.id, tipo_agendamento, confirmado, foi_faturado, foi_cancelado, hora_fim, hora_inicio, dt_agenda, cod_fatura,
    motivo, resultado, nome_lab
    FROM
    ((((((((((cliente LEFT JOIN animal ON CPF=cpf_cliente) LEFT JOIN envolve ON id_animal=ida)
    LEFT JOIN agendamento ON id_agend=id) 
    LEFT JOIN envolve_ac ON id_agendclin=id) 
    LEFT JOIN envolve_ap ON id_agendpet=id)
    LEFT JOIN envolve_al ON id_agendlab=id)
    LEFT JOIN servico_petshop ON cod_servpet= servico_petshop.cod )
    LEFT JOIN servico_clinica ON cod_servclin= servico_clinica.cod )
    LEFT JOIN laboratorial ON cod_servlab= laboratorial.cod)
    LEFT JOIN diagnostico ON laboratorial.cod=cod_lab)
    
    
    WHERE CPF=fgetcpf();
    
    END //
CALL relatoriocli('130642302');

CALL grantCli('101286373');
 DELIMITER //
 CREATE PROCEDURE grantCli (IN logincpf CHAR(12))
		BEGIN
		 SET @logincpf = logincpf; 
         DROP VIEW IF EXISTS vanimaiscli;
         CREATE VIEW vanimaiscli AS SELECT * FROM animal WHERE cpf_cliente=substring_index(user(),'@',1); 
         DROP VIEW IF EXISTS vcli;
         CREATE VIEW vcli AS SELECT * FROM cliente WHERE CPF=substring_index(user(),'@',1);
         DROP VIEW IF EXISTS valergico;
         CREATE VIEW valergico AS SELECT * FROM alergico WHERE id_animal IN (SELECT ida FROM vanimaiscli);
         
         SET @query=fgrantq('INSERT,SELECT,UPDATE,DELETE','vanimaiscli',@logincpf);
         PREPARE stmt FROM @query;
		 EXECUTE stmt;
         FLUSH PRIVILEGES;
         SET @query=fgrantq('SELECT','raça',@logincpf);
         PREPARE stmt FROM @query;
		 EXECUTE stmt;
         FLUSH PRIVILEGES;
         SET @query=fgrantq('INSERT,SELECT,UPDATE,DELETE','valergico',@logincpf);
         PREPARE stmt FROM @query;
		 EXECUTE stmt;
         FLUSH PRIVILEGES;
         SET @query=fgrantq('SELECT','substancia',@logincpf);
         PREPARE stmt FROM @query;
		 EXECUTE stmt;
         FLUSH PRIVILEGES;
         SET @query=fgrantq('SELECT','vcli',@logincpf);
		 PREPARE stmt FROM @query;
		 EXECUTE stmt;
         FLUSH PRIVILEGES;
		 DEALLOCATE PREPARE stmt;
		 
		END //

DELIMITER //
CREATE PROCEDURE grantFunc (IN logincpf CHAR(12))
	BEGIN
    SET @logincpf = logincpf;
    
    SET @query=fgrantq('INSERT,SELECT,UPDATE,DELETE','envolve',@logincpf);
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	FLUSH PRIVILEGES;
	SET @query=fgrantq('INSERT,SELECT,UPDATE,DELETE','realiza',@logincpf);
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	FLUSH PRIVILEGES;
    SET @query=fgrantq('INSERT,SELECT,UPDATE,DELETE','fatura',@logincpf);
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	FLUSH PRIVILEGES;
    SET @query=fgrantq('SELECT','animal',@logincpf);
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	FLUSH PRIVILEGES;
    SET @query=fgrantq('SELECT','tipo_servico',@logincpf);
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	FLUSH PRIVILEGES;
    SET @query=fgrantq('SELECT','almoxarifado',@logincpf);
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	FLUSH PRIVILEGES;
    SET @query=fgrantq('SELECT','instancia',@logincpf);
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	FLUSH PRIVILEGES;
    SET @query=fgrantq('SELECT','produto_ref',@logincpf);
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	FLUSH PRIVILEGES;
    SET @query=fgrantq('INSERT,SELECT,UPDATE,DELETE','agendamento',@logincpf);
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	FLUSH PRIVILEGES;
    SET @query=fgrantq('INSERT,SELECT,UPDATE,DELETE','cliente',@logincpf);
	PREPARE stmt FROM @query;
	EXECUTE stmt;
    FLUSH PRIVILEGES;
	SET @query=CONCAT('GRANT EXECUTE ON PROCEDURE carepet.agendamentoCLI TO ''',@logincpf,'''@''localhost'';');
	PREPARE stmt FROM @query;
	EXECUTE stmt;
	FLUSH PRIVILEGES;
	DEALLOCATE PREPARE stmt;
    END //

-- DROP PROCEDURE existUser;
-- DROP PROCEDURE grantCli;
-- DROP VIEW vanimaiscli;
-- DROP FUNCTION logincpf;

CREATE TABLE ALMOXARIFADO(
id bigint(7) primary key AUTO_INCREMENT, 
descr varchar(100) not null
);
CREATE TABLE ESTABELECIMENTO (
codigo bigint(7) AUTO_INCREMENT, 
CNPJ char(14) not null, 
nome_fantasia varchar(30) not null, 
cidade varchar(30) not null, 
email varchar(50) not null, 
CEP char(8) not null, 
logradouro varchar(50) not null, 
telefone integer(11) not null, 
hora_fecha time not null, 
hora_abertura time not null, 
id_almox bigint(7) not null,
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
confirmadoc boolean default false,
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
cod_est bigint(7) not null, 
senha varchar(15) not null,
CRMV varchar(6) unique,
tipo_fun enum('Tecnico', 'Veterinario') not null,
primary key (CPF),
foreign key(cod_est) references ESTABELECIMENTO(Codigo));
CREATE TABLE HORARIO(
id bigint(7) AUTO_INCREMENT, 
hr_inicio time not null, 
hr_fim time not null, 
dia_semana enum('Domingo','Segunda-feira','Terça-feira','Quarta-feira','Quinta-feira','Sexta-feira','Sábado') not null,
periodo enum('matutino', 'vespertino', 'noturno', 'plantão') not null,
primary key (id));
CREATE TABLE ESPECIALIDADE(
id bigint(7) AUTO_INCREMENT, 
descricao varchar(60) not null,
primary key (id));
CREATE TABLE RAÇA(
cod bigint(7) AUTO_INCREMENT, 
porte_animal enum('pequeno porte', 'medio porte', 'grande porte') not null, 
descr varchar(70) not null, 
obs varchar(60),
primary key (cod));
CREATE TABLE ANIMAL(
ida bigint(7) AUTO_INCREMENT, 
nomea varchar(20) not null, 
dt_nasca date not null,
idade integer(2) not null, 
cpf_cliente char(12) not null, 
cod_raça bigint(7) not null,
primary key (ida),
foreign key(cpf_cliente) references CLIENTE(CPF) ON DELETE CASCADE,
foreign key(cod_raça) references RAÇA (cod));
CREATE TABLE SUBSTANCIA(
cod bigint(7) AUTO_INCREMENT,  
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
cod varchar(10) , 
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
id bigint(16) AUTO_INCREMENT, 
tipo_agendamento enum('PET', 'CLI', 'LAB') not null, 
confirmado boolean default false, 
foi_faturado boolean default false, 
foi_cancelado boolean default false, 
hora_fim time not null, 
hora_inicio time not null, 
dt_agenda date not null, 
cod_fatura bigint(10) not null,
primary key (id),
foreign key(cod_fatura) references FATURA(cod) ON DELETE CASCADE);
CREATE TABLE TIPO_SERVICO(
cod bigint(7) AUTO_INCREMENT, 
categoria enum('cirurgico', 'exame', 'estético') not null, 
descr varchar(70) not null,
precoinicial numeric(6,2) not null,
primary key (cod));
CREATE TABLE SERVICO(
cod bigint(7) AUTO_INCREMENT, 
obs varchar(70), 
preco numeric(6,2)  not null, 
dt_solicitacao date not null, 
dt_realizacao date not null, 
duracao integer(2) not null, 
cod_tipo bigint(7) not null,
primary key (cod),
check (preco > 0),
foreign key(cod_tipo) references TIPO_SERVICO(cod));
CREATE TABLE SERVICO_PETSHOP(
cod bigint(7) AUTO_INCREMENT,
primary key (cod),
foreign key(cod) references SERVICO(cod) ON DELETE CASCADE);
CREATE TABLE SERVICO_CLINICA(
cod bigint(7) AUTO_INCREMENT, 
motivo varchar(70) not null, 
resultado varchar(70) not null default 'Não Divulgado',
primary key (cod),
foreign key(cod) references SERVICO(cod) ON DELETE CASCADE);
CREATE TABLE LABORATORIAL(
cod bigint(7) AUTO_INCREMENT, 
nome_lab varchar(30) not null, 
fone_lab integer(11) not null,
primary key (cod),
foreign key(cod) references SERVICO(cod) ON DELETE CASCADE);
CREATE TABLE DIAGNOSTICO(
id varchar(10), 
obs varchar(70), 
descrição varchar(100) not null, 
grau_urgencia enum('baixo', 'normal', 'grave', 'gravíssimo') not null, 
cod_lab bigint(7) not null,
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
cod integer(2), 
descr varchar(30) not null,
primary key (cod));
CREATE TABLE PAGAMENTO(
id bigint(16) AUTO_INCREMENT, 
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
cod bigint(7) AUTO_INCREMENT, 
descr varchar(50) not null,
primary key (cod));
CREATE TABLE INSTANCIA(
seq varchar(10), 
cod_barra integer(13) not null, 
dt_fabricacao date, 
dt_validade date, 
qtd_atual integer(4) not null, 
id_almox bigint(7) not null,
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
cod_cat bigint(7) not null,
seq_inst varchar(10) UNIQUE,
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
id_agend bigint(16) AUTO_INCREMENT, 
data_marcada date not null,
primary key (cpf_cliente, cpf_func, id_agend),
foreign key(cpf_cliente) references CLIENTE(CPF),
foreign key(cpf_func) references FUNCIONARIO(CPF),
foreign key(id_agend) references AGENDAMENTO(id) ON DELETE CASCADE);
CREATE TABLE POSSUI_HOR(
CPF_func char(11), 
id_horario bigint(7),
primary key (CPF_func, id_horario),
foreign key(CPF_func) references FUNCIONARIO(CPF),
foreign key(id_horario) references HORARIO(id));
CREATE TABLE TEM_ESP(
CPF_vet char(11), 
id_esp bigint(7),
primary key (CPF_vet, id_esp),
foreign key(CPF_vet) references FUNCIONARIO(CPF),
foreign key(id_esp) references ESPECIALIDADE(id));
CREATE TABLE ALERGICO(
id_animal bigint(7), 
cod_substancia bigint(7),
primary key (id_animal, cod_substancia),
foreign key(id_animal) references ANIMAL(ida),
foreign key(cod_substancia) references SUBSTANCIA(cod));
CREATE TABLE ENVOLVE(
id_animal bigint(7),
id_agend bigint(16) AUTO_INCREMENT,
primary key (id_animal, id_agend),
foreign key(id_animal) references ANIMAL(ida),
foreign key (id_agend) references AGENDAMENTO(id) ON DELETE CASCADE);
CREATE TABLE ENVOLVE_AP(
id_agendpet bigint(16) AUTO_INCREMENT, 
cod_servpet bigint(7),
primary key (id_agendpet, cod_servpet),
foreign key(id_agendpet) references AGENDAMENTO(id) ON DELETE CASCADE,
foreign key(cod_servpet) references SERVICO_PETSHOP(cod));
CREATE TABLE ENVOLVE_AC(
id_agendclin bigint(16) AUTO_INCREMENT, 
cod_servclin bigint(7),
primary key (id_agendclin, cod_servclin),
foreign key(id_agendclin) references AGENDAMENTO(id) ON DELETE CASCADE,
foreign key(cod_servclin) references SERVICO_CLINICA(cod));
CREATE TABLE ENVOLVE_AL(
id_agendlab bigint(16) AUTO_INCREMENT,
cod_servlab bigint(7),
primary key (id_agendlab, cod_servlab),
foreign key(id_agendlab) references AGENDAMENTO(id) ON DELETE CASCADE,
foreign key(cod_servlab) references LABORATORIAL (cod));
CREATE TABLE FORNECE_CL(
cod_clin bigint(7), 
cod_servc bigint(7),
primary key (cod_clin, cod_servc),
foreign key(cod_clin) references ESTABELECIMENTO(codigo),
foreign key(cod_servc) references SERVICO_CLINICA(cod));
CREATE TABLE FORNECE_PET(
cod_pet bigint(7), 
cod_servp bigint(7),
primary key (cod_pet, cod_servp),
foreign key(cod_pet) references ESTABELECIMENTO(codigo),
foreign key(cod_servp) references SERVICO_PETSHOP(cod));
