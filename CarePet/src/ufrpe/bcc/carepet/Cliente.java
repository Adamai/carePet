package ufrpe.bcc.carepet;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Cliente {
	private String nome;
	private String CPF;
	private String email;
	private String dtnasc;
	private String cidade;
	private String CEP;
	private String logradouro;
	private String dtUltLogin;
	private String cartCred;
	private String confirm;
	private String senha;

	Cliente(ResultSet cli){
		try{
			cli.first();
			nome = cli.getString("nome");
			CPF = cli.getString("CPF");
			email = cli.getString("email");
			dtnasc = cli.getString("dt_nasc");
			cidade = cli.getString("cidade");
			CEP = cli.getString("CEP");
			logradouro = cli.getString("Logradouro");
			dtUltLogin = cli.getString("dt_ultimo_login");
			cartCred = cli.getString("cartão_cred");
			confirm = cli.getString("confirmadoc");
			senha = cli.getString("senha");
			} catch (SQLException e) {
				System.out.println("Houve erro no acesso das informacoes (objeto Cliente)");
				e.printStackTrace();
			}
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getCPF() {
		return CPF;
	}

	public void setCPF(String cPF) {
		CPF = cPF;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDtnasc() {
		return dtnasc;
	}

	public void setDtnasc(String dtnasc) {
		this.dtnasc = dtnasc;
	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getCEP() {
		return CEP;
	}

	public void setCEP(String cEP) {
		CEP = cEP;
	}

	public String getLogradouro() {
		return logradouro;
	}

	public void setLogradouro(String logradouro) {
		this.logradouro = logradouro;
	}

	public String getDtUltLogin() {
		if(dtUltLogin==null)
			return "Nenhum login ainda feito";
		else
			return dtUltLogin;
	}

	public void setDtUltLogin(String dtUltLogin) {
		this.dtUltLogin = dtUltLogin;
	}

	public String getCartCred() {
		if(cartCred == null)
			return "Nao possui cadastrado";
		else
			return cartCred;
	}

	public void setCartCred(String cartCred) {
		this.cartCred = cartCred;
	}

	public String getConfirm() {
		return confirm;
	}

	public void setConfirm(String confirm) {
		this.confirm = confirm;
	}
	
	public String getSenha() {
		return senha;
	}

	

}
