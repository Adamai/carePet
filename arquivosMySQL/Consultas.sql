SELECT nome, v_total, qtd, dia  FROM ((CLIENTE INNER JOIN pedido_produto) INNER JOIN ITEM_PRODUTO) ORDER BY nome;

SELECT nomea, cod, dt_agenda, vl_total
    FROM (fatura JOIN agendamento ON cod=cod_fatura) JOIN 
    (envolve JOIN (SELECT * FROM animal ) AS animaiscli ON id_animal=ida)
    ON id=id_agend;
    
SELECT descr,qtd_atual FROM (produto_ref INNER JOIN instancia ON seq=seq_inst) INNER JOIN almoxarifado
    ON id=id_almox ;