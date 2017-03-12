package ufrpe.bcc.carepet;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Funcionario {
	private String nome;
	private String CPF;
	private String email;
	private String dtnasc;
	private String cidade;
	private String CEP;
	private String logradouro;
	private String dtadmis;
	private String codEstab;
	private String CRMV;
	private String tipofunc;
	
	Funcionario(ResultSet Func){
		try{
		Func.first();
		nome = Func.getString("nome");
		CPF = Func.getString("CPF");
		email = Func.getString("email");
		dtnasc = Func.getString("dt_nasc");
		cidade = Func.getString("cidade");
		CEP = Func.getString("CEP");
		logradouro = Func.getString("Logradouro");
		dtadmis = Func.getString("dt_admissao");
		codEstab = Func.getString("cod_est");
		CRMV = Func.getString("CRMV");
		tipofunc = Func.getString("tipo_fun");
		} catch (SQLException e) {
			System.out.println("Houve erro no acesso das informacoes (objeto Funcionario)");
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

	public String getDtadmis() {
		return dtadmis;
	}

	public void setDtadmis(String dtadmis) {
		this.dtadmis = dtadmis;
	}

	public String getCodEstab() {
		return codEstab;
	}

	public void setCodEstab(String codEstab) {
		this.codEstab = codEstab;
	}

	public String getCRMV() {
		if(CRMV==null)
			return "Nao possui";
		else
			return CRMV;
	}

	public void setCRMV(String cRMV) {
		CRMV = cRMV;
	}

	public String getTipofunc() {
		return tipofunc;
	}

	public void setTipofunc(String tipofunc) {
		this.tipofunc = tipofunc;
	}
	


}
