/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.swing.JOptionPane;
/**
 *
 * @author Sufyan
 */
public class AccountDAO {
     Connection con=null;

    void Connection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/atm", "root", "");
        } catch (ClassNotFoundException | SQLException ex) {
            
        }
    }
    
    int insert(Account a){
        Connection();
        int res=0;
        try{
            String qry="INSERT INTO `account`(`Username`, `Password`, `Name`, `Gender`, `Initial Account`,`Balance`) VALUES (?,?,?,?,?,?)";
            PreparedStatement pst=con.prepareStatement(qry);
            pst.setString(1,a.username);
            pst.setString(2,a.pass);
            pst.setString(3,a.name);
            pst.setString(4,a.gender);
            pst.setString(5,a.acc);
            pst.setInt(6, 0);
            res=pst.executeUpdate();
            
        }
        catch(SQLException ex)
        {
            JOptionPane.showMessageDialog(null, ex);
        }
        return res;
    }
    
    int delete(Account a){
        Connection();
        int res=0;
        try
        {
            String qry="DELETE FROM `account` WHERE  `Username`=?";
            PreparedStatement pst=con.prepareStatement(qry);
            
            pst.setString(1,a.username);
            res=pst.executeUpdate();
            
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        return res;
    }
    
    int update(Account a){
        Connection();
        int res=0;
        try
        {
            String qry="UPDATE `account` SET `Password`=?,`Name`=?,`Gender`=?,`Initial Account`=? WHERE `Username`=?";
            PreparedStatement pst=con.prepareStatement(qry);
            
            
            pst.setString(1,a.pass);
            pst.setString(2,a.name);
            pst.setString(3, a.gender);
            pst.setString(4, a.acc);
            pst.setString(5,a.username);
            res=pst.executeUpdate();
            
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        return res;
    }
    
    int updateBalance(Account a){
        Connection();
        int res=0;
        try
        {
            String qry="UPDATE `account` SET `Balance`=? WHERE `Username`=?";
            PreparedStatement pst=con.prepareStatement(qry);
            
            pst.setInt(1,a.balance);
            pst.setString(2,a.username);
            res=pst.executeUpdate();
            
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        return res;
    }
    int billPayment(Account a){
        Connection();
        ResultSet res= null;
        int rs=0;
        try
        {
            String qry="SELECT * FROM `account` WHERE `Username`=? ";
            PreparedStatement pst=con.prepareStatement(qry);
            pst.setString(1,a.username);
            res=pst.executeQuery();
            
            if(res.next()){
                if(a.balance>Integer.parseInt(res.getString("Balance"))){
                    JOptionPane.showMessageDialog(null,"Insufficient Balance");
                }
                else
                {
                    String query="UPDATE `account` SET `Balance`=? WHERE `Username`=?";
                    PreparedStatement pt=con.prepareStatement(query);
                    a.balance=Integer.parseInt(res.getString("Balance"))-a.balance;
                    pt.setInt(1,a.balance);
                    pt.setString(2,a.username);
                    rs=pt.executeUpdate();
                }
            }
        }
        catch(HeadlessException | NumberFormatException | SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        return rs;
    }
    
    
    
    ResultSet search(Account a){
        Connection();
        ResultSet res=null;
        try
        {
            String qry="SELECT * FROM `account` WHERE `Username`=? ";
            PreparedStatement pst=con.prepareStatement(qry);
            
            pst.setString(1,a.username);
            
            res=pst.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        
        return res;
    }
    
    
    ResultSet table(){
        Connection();
        ResultSet res=null;
        try
        {
            String qry="SELECT * FROM `account`";
            PreparedStatement pst=con.prepareStatement(qry);
            
            res=pst.executeQuery();
        }
        catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        
        return res;
    }
    
    
    int transfer(Account a){
        Connection();
        ResultSet res=null;
        ResultSet rs=null;
        int r=0;
        try
        {
            String qry="SELECT * FROM `account` WHERE `Username`=? ";
            PreparedStatement pst=con.prepareStatement(qry);
            pst.setString(1,a.username);
            res=pst.executeQuery();
            
            String query="SELECT * FROM `account` WHERE `Username`=? ";
            PreparedStatement pt=con.prepareStatement(query);
            pt.setString(1,a.username2);
            rs=pt.executeQuery();
            
            if(res.next()&&rs.next())
            {
                if(Integer.parseInt(res.getString("Balance"))>=a.balance){
                    a.user1Bal=Integer.parseInt(res.getString("Balance"))-a.balance;
                    
                    String qy="UPDATE `account` SET `Balance`=? WHERE `Username`=?";
                    PreparedStatement p=con.prepareStatement(qy);
                    
                    p.setInt(1,a.user1Bal);
                    p.setString(2,a.username);
                    p.executeUpdate();
                    
                    a.user2Bal=a.balance+Integer.parseInt(rs.getString("Balance"));
                    String q="UPDATE `account` SET `Balance`=? WHERE `Username`=?";
                    PreparedStatement pre=con.prepareStatement(q);
                    pre.setInt(1,a.user2Bal);
                    pre.setString(2,a.username2);
                    pre.executeUpdate();
                    
                    
                    
                    JOptionPane.showMessageDialog(null, "Transfer Succesful");
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Insufficient Balance");
                }
            }
            
        }
        catch(HeadlessException | NumberFormatException | SQLException ex){
            JOptionPane.showMessageDialog(null, ex);
        }
        return r;
    }
    
}
