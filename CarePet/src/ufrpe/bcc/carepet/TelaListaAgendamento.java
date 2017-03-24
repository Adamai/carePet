package ufrpe.bcc.carepet;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.JScrollPane;
import javax.swing.JList;
import javax.swing.DefaultListModel;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.JLabel;
import javax.swing.JButton;
import java.awt.event.ActionListener;

public class TelaListaAgendamento extends JFrame implements ActionListener {

	private JPanel contentPane;
	private Funcionario func;
	private JList listClientes = new JList();
	private JList listAgendamentos = new JList();
	private DefaultListModel modelCl = new DefaultListModel();
	private DefaultListModel modelAg = new DefaultListModel();
	private JButton btnVoltar;
	private JButton btnConfirmarMod;
	private JButton btnDeletar;
	private JButton btnModificar;
	
	
static {
		
		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (Exception e) {
			System.out.println("Problemas carregando o Driver do MySQL");
		}
	}
	
	public static Connection getConexao() throws SQLException {
		
		Connection retorno = null;
		retorno = DriverManager.getConnection(
				"jdbc:mysql://localhost:3306/carepet?autoReconnect=true&useSSL=false", "root", "paloma"); // nome do esquema, usuário e senha
		return retorno;
	}
	/**
	 * Create the frame.
	 */
	public TelaListaAgendamento(Funcionario funcionario) {
		int ilista1 = 0;
		
		try{
			Connection conex = getConexao();
			String query = "	SELECT * FROM cliente;";
			PreparedStatement psGetCli = conex.prepareStatement(query);
			ResultSet rsGetCli = psGetCli.executeQuery();
			while(rsGetCli.next()){
				modelCl.addElement(rsGetCli.getString("nome"));
				ilista1++;
			}
			psGetCli.close();
			conex.close();
		} catch (SQLException e) {
			System.out.println("Houve erro");
			e.printStackTrace();
		}
		if(ilista1!=0){
			listClientes.setModel(modelCl);
		}
		
		listClientes.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent arg0) { //pegar nome->CPF, CPF->realiza, realiza->agendamento
				//ResultSet rsagenid;
				
				modelAg = new DefaultListModel();
				if (!arg0.getValueIsAdjusting()) {
	              int ilista2 = 0;
	              ResultSet rsagend = null;
	              btnModificar.setEnabled(true);
	              btnDeletar.setEnabled(true);
	              try{
	      			Connection conex = getConexao();
	      			PreparedStatement psagenid = conex.prepareStatement("SELECT id_agend FROM realiza WHERE "
	      					+ "cpf_cliente=(SELECT CPF FROM cliente WHERE nome = '"+listClientes.getSelectedValue().toString()+"');");
	      			ResultSet rsagenid = psagenid.executeQuery();
	      			while(rsagenid.next()){
	      				String query = "SELECT * FROM agendamento WHERE id="+rsagenid.getString("id_agend")+" ;";
	      				Statement s = conex.createStatement();
	      				rsagend = s.executeQuery(query);
	      				while(rsagend.next()){
	      					ilista2++;
	      					String confirm = "";
	      					String faturado = "";
	      					String cancelado = "";
	      					if(rsagend.getString("confirmado")=="true")
	      						confirm = "Sim";
	      					else
	      						confirm = "Não";
	      					if(rsagend.getString("foi_efetivado")=="true")  //ALTERAR PARA FATURADO
	      						faturado = "Sim";
	      					else
	      						faturado = "Não";
	      					if(rsagend.getString("foi_cancelado")=="true")
	      						cancelado = "Sim";
	      					else
	      						cancelado = "Não";
	      					modelAg.addElement("D:   M:   ID: "+rsagend.getString("id")+" | Tipo: "
	      							+rsagend.getString("tipo_agendamento")+" | Confirmado: "+confirm+
	      							" | Faturado: "+faturado+" | Cancelado: "+cancelado+" | Data do agendamento: "+rsagend.getString("dt_agenda")+
	      							" | Hora Inicio: "+rsagend.getString("hora_inicio")+
	      							" | Hora fim: "+rsagend.getString("hora_fim")+" | Código de Fatura: "+
	      							rsagend.getString("cod_fatura"));
	      					
	      					
	      				}
	      				
	      			}
	      			if(ilista2!=0)                
	      				listAgendamentos.setModel(modelAg);
	      			if(rsagend != null){
	      				rsagend.close();
	      			} else {
	      				modelAg.removeAllElements();
	      				listAgendamentos.setModel(modelAg);
	      			}
	      			rsagenid.close();
	      			psagenid.close();
	      			conex.close();
	      		} catch (SQLException e) {
	      			System.out.println("Houve erro");
	      			e.printStackTrace();
	      		}
	              
	            }
	        }
		});
		
		
		func = funcionario;
		setTitle("Logado como: "+func.getNome());
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 600, 550);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		
		JScrollPane scrollPane_1 = new JScrollPane();
		
		btnVoltar = new JButton("Voltar");
		btnVoltar.addActionListener(this);
		
		btnConfirmarMod = new JButton("Confirmar Modifica\u00E7\u00F5es");
		btnConfirmarMod.setEnabled(false);
		btnConfirmarMod.addActionListener(this);
		
		btnDeletar = new JButton("Deletar");
		btnDeletar.setEnabled(false);
		
		btnModificar = new JButton("Modificar");
		btnModificar.setEnabled(false);
		GroupLayout gl_contentPane = new GroupLayout(contentPane);
		gl_contentPane.setHorizontalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addContainerGap()
					.addGroup(gl_contentPane.createParallelGroup(Alignment.LEADING)
						.addComponent(scrollPane, GroupLayout.DEFAULT_SIZE, 554, Short.MAX_VALUE)
						.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 554, GroupLayout.PREFERRED_SIZE)
						.addGroup(gl_contentPane.createSequentialGroup()
							.addComponent(btnVoltar)
							.addPreferredGap(ComponentPlacement.RELATED, 404, Short.MAX_VALUE)
							.addComponent(btnConfirmarMod))
						.addGroup(Alignment.TRAILING, gl_contentPane.createSequentialGroup()
							.addComponent(btnModificar)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addComponent(btnDeletar)))
					.addContainerGap())
		);
		gl_contentPane.setVerticalGroup(
			gl_contentPane.createParallelGroup(Alignment.LEADING)
				.addGroup(gl_contentPane.createSequentialGroup()
					.addGap(32)
					.addComponent(scrollPane, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addComponent(scrollPane_1, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE)
					.addPreferredGap(ComponentPlacement.UNRELATED)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnDeletar)
						.addComponent(btnModificar))
					.addPreferredGap(ComponentPlacement.RELATED, 11, Short.MAX_VALUE)
					.addGroup(gl_contentPane.createParallelGroup(Alignment.BASELINE)
						.addComponent(btnVoltar)
						.addComponent(btnConfirmarMod))
					.addContainerGap())
		);
		
		//listAgendamentos = new JList();
		scrollPane_1.setViewportView(listAgendamentos);
		
		JLabel lblAgendamentos = new JLabel("Deletado Modificado                                    Agendamentos ");
		scrollPane_1.setColumnHeaderView(lblAgendamentos);
		
		//listClientes = new JList();
		scrollPane.setViewportView(listClientes);
		
		JLabel lblClientes = new JLabel("Clientes");
		scrollPane.setColumnHeaderView(lblClientes);
		contentPane.setLayout(gl_contentPane);
	}
	
	public void actionPerformed(ActionEvent evento){
		if(evento.getSource().equals(btnVoltar)){
			dispose();
			TelaFuncionario tela = new TelaFuncionario(func);
			tela.setVisible(true);
		}
		if(evento.getSource().equals(btnDeletar)){ // marcar como deletar
			
		}
		if(evento.getSource().equals(btnModificar)){  //abrir outra janela para modificar
			
		}
		if(evento.getSource().equals(btnConfirmarMod)){ //checar por d: m: em cada cell da lista
			
		}
	}
	
}
