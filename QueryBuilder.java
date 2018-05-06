import java.sql.*;
import javax.sql.*;
import javax.sql.rowset.*;
import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.*;
class QueryBuilder implements ActionListener,ItemListener
{
	JLabel l1,l2,l3,l4,l5,l6;
	 JList j1,j2;
	JComboBox jc1,jc2,jc3;
	 JCheckBox jcb;
    JButton jb1,jb2,jb3,jb4,jb5;
	JTextField jf1,jf2;
	JFrame f;
	JTable table;
	JPanel jp;
	String query;
	ResultSet rs;
	Statement st;
	Connection conn;
	DefaultListModel dlm;
	Vector<String>le1,le;
	Set<String>le2,les;
	String tableName;
	
	java.util.List le3;
	int conditionsApplied=0;
	JDesktopPane desktop;
	QueryBuilder()
	{
		f=new JFrame("QueryBuilder for Oracle DB");
		desktop=new JDesktopPane();
		desktop.setBackground(Color.lightGray);
		
		
		f.setContentPane(desktop);
		l1=new JLabel("Select Table:",0);
		l1.setBounds(10,0,80,30);
		f.add(l1);
		jc1=new JComboBox();
		jc1.addActionListener(this);
		jc1.setBounds(100,5,170,20);
		f.add(jc1);
		le=new Vector<String>();
		le1=new Vector<String>();
		le2=new HashSet<String>();
		les=new HashSet<String>();
		le3=new Vector<String>();
		j1=new JList(le1);
		j1.setBounds(20,50,150,150);
		f.add(j1);
		jb1=new JButton("Add");
		jb1.setBounds(200,60,100,30);
		jb1.addActionListener(this);
		f.add(jb1);
		jb2=new JButton("Remove");
		jb2.setBounds(200,100,100,30);
		jb2.addActionListener(this);
		f.add(jb2);
		jb3=new JButton("Add All");
		jb3.setBounds(200,140,100,30);
		jb3.addActionListener(this);
		f.add(jb3);
		j2=new JList(le);
		j2.setBounds(330,50,150,150);
		f.add(j2);
		l2=new JLabel("Conditions",0);
		l2.setBounds(10,220,80,30);
		f.add(l2);
		jcb=new JCheckBox();
		jcb.setBounds(80,225,20,20);
		jcb.addItemListener(this);
		f.add(jcb);
		l3=new JLabel("Apply Condition On",0);
		l3.setBounds(10,200,150,150);
		f.add(l3);
		jc2=new JComboBox();
		jc2.setBounds(150,265,170,20);
		jc2.setEnabled(false);
		f.add(jc2);
		l4=new JLabel("Operator",0);
		l4.setBounds(10,235,150,150);
		f.add(l4);
		jc3=new JComboBox();
		jc3.setBounds(150,300,170,20);
		jc3.setEnabled(false);
		f.add(jc3);
		l5=new JLabel("Value",0);
		l5.setBounds(10,270,150,150);
		f.add(l5);
		jf1=new JTextField();
		jf1.setBounds(150,335,150,20);
		jf1.setEnabled(false);
		f.add(jf1);
		jf2=new JTextField();
		jf2.setBounds(10,370,370,60);
		jf2.setEditable(false);
		jf2.setText("final Query will be shown here");
		f.add(jf2);
		jb4=new JButton("Show Query");
		jb4.setBounds(400,370,110,30);
		jb4.addActionListener(this);
		f.add(jb4);
		jb5=new JButton("Show Result");
		jb5.setBounds(200,450,110,30);
		jb5.addActionListener(this);
		
		f.add(jb5);
		f.setLayout(null);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		f.setSize(550,600);
		f.setVisible(true);
		
	}
	public void itemStateChanged(ItemEvent e) 
	{
        
        Object source = e.getItemSelectable();
		if(source==jcb)
		{
			if(conditionsApplied%2==0)
			{
				conditionsApplied=1;
			jf1.setEnabled(true);
			jc2.setEnabled(true);
			jc3.setEnabled(true);
			Iterator<String> itr = le.iterator();
			while(itr.hasNext())
			{
				jc2.addItem((String)itr.next());
				
			}
			jc3.addItem("=");
			jc3.addItem(">");
			jc3.addItem("<");
			jc3.addItem(">=");
			jc3.addItem("<=");
			jc3.addItem("!=");
			jc3.addItem("and");
			jc3.addItem("or");
			jc3.addItem("like");
			jc3.addItem("between");
			}
			else
			{
				conditionsApplied=0;
			jf1.setEnabled(false);
			jc2.setEnabled(false);
			jc3.setEnabled(false);
			}
		}
	}
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource() instanceof JComboBox)
		{
			JComboBox jcb=(JComboBox)e.getSource();
			String table_name=(String)jcb.getSelectedItem();
			
			try
			{
				showColumns(table_name);
			}
			catch(Exception e1)
			{}
			
		}
		else if(e.getSource()==jb1)
		{
			le3=j1.getSelectedValuesList();
			
			le2.clear();
			le2.addAll(le3);
			les.addAll(le2);
			le.clear();
			le.addAll(les);
			this.j2.setListData(le);
		}
		else if(e.getSource()==jb3)
		{
			le2.clear();
			les.clear();
			le.clear();
			le3.clear();
			le2.addAll(le1);
			les.addAll(le2);
			le.addAll(les);
			this.j2.setListData(le);
		}
		else if(e.getSource()==jb2)
		{
			le.remove(j2.getSelectedValue());
		
			this.j2.setListData(le);
			
		}
		else if(e.getSource()==jb4)
		{
			query="select ";
			if(le.size()==le1.size())
			{
				query+="* ";
			}
			else
			{
				Iterator<String> itr = le.iterator();
				while(itr.hasNext())
				{
					query=query+(String)itr.next();
					if(itr.hasNext())
					{
						query+=",";
					}
					else
					{
						query+=" ";
					}
				
				}
			}
			query+="from "+tableName+" ";
			if(conditionsApplied==1)
			{
				query=query+"where "+(String)jc2.getSelectedItem()+" "+(String)jc3.getSelectedItem()+" "+jf1.getText();
			}
			
			jf2.setText(query);
		}
		else if(e.getSource()==jb5)
		{
			query="select ";
			if(le.size()==le1.size())
			{
				query+="* ";
			}
			else
			{
				Iterator<String> itr = le.iterator();
				while(itr.hasNext())
				{
					query=query+(String)itr.next();
					if(itr.hasNext())
					{
						query+=",";
					}
					else
					{
						query+=" ";
					}
				
				}
			}
			query+="from "+tableName+" ";
			if(conditionsApplied==1)
			{
				query=query+"where "+(String)jc2.getSelectedItem()+" "+(String)jc3.getSelectedItem()+" "+jf1.getText();
			}
			
			jf2.setText(query);
			//begin
			 ArrayList data = new ArrayList();
			try
			{
				st=conn.createStatement();
				rs=st.executeQuery(query);
				
				
				int columns=le.size();
				Vector<String> columnsName=new Vector<String>();
				Iterator<String> itr = le.iterator();
				
				while(itr.hasNext())
				{
					//System.out.println(itr.next());
					columnsName.add(itr.next());
					
				}
				 
				while (rs.next())
            {
				System.out.println("loop");
			   ArrayList row = new ArrayList(columns);

                for (int i = 1; i<=columns; i++)
                {
					row.add(rs.getObject(i));
                }

                data.add(row);
            }
			System.out.println(data);
			Vector dataVector = new Vector();

        for (int i = 0; i < data.size(); i++)
        {
            ArrayList subArray = (ArrayList)data.get(i);
            Vector subVector = new Vector();
            for (int j = 0; j < subArray.size(); j++)
            {
                subVector.add(subArray.get(j));
            }
            dataVector.add(subVector);
        }
			System.out.println(dataVector);
			table = new JTable(dataVector, columnsName);
			
			
			}
			catch(Exception e1)
			{
				System.out.println(e1);
			}
			JInternalFrame frame;
			frame=new JInternalFrame("Result ",true, true, true, true);
			frame.setSize(500,500);
			
			JScrollPane scrollPane = new JScrollPane(table);
			frame.add(scrollPane);
			desktop.add(frame);
			frame.setVisible(true);
			frame.moveToFront();
			
		}
	}
	void showColumns(String table_name)throws Exception
	{
		tableName=table_name;
		DatabaseMetaData dbmd=conn.getMetaData();
		ResultSet table=dbmd.getColumns(null,null,table_name,null);
		le1.clear();
		le2.clear();
		le.clear();
		le3.clear();
		les.clear();
		while(table.next())
		{
			
			
			le1.add(table.getString("COLUMN_NAME"));
		}
		this.j1.setListData(le1);
		
		this.f.repaint();
	}
	
	
	public static void main(String... s) throws Exception
	{
		QueryBuilder qb=new QueryBuilder();
		Class.forName("oracle.jdbc.driver.OracleDriver");
		qb.conn=DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:XE","system","123456");
		String[] TABLES_TYPES={"TABLE","VIEW"};
		DatabaseMetaData dbmd=qb.conn.getMetaData();
		ResultSet tables=dbmd.getTables(null,null,null,TABLES_TYPES);
		ResultSetMetaData rsmd=tables.getMetaData();
		while(tables.next())
		{
			qb.jc1.addItem(tables.getString(3));	
		}
	
	}
}