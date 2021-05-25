package com.fjp.dao;

import com.fjp.entity.Product;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class AddProducttoTXT implements AddProduct {

	@Override
	public boolean addProduct(Product product) {
		String s = product.getId()+ " " +product.getName()+" "+product.getPrice()+" "+product.getUnit()+" "+product.getSort()+ " "+
		" "+product.getCount();
		File file = new File("d:\\addedproducts.txt");
		try {
			FileWriter fw = new FileWriter(file);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(s);
			bw.newLine();
			bw.flush();
	    
			bw.close();
		}catch(IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();}
		
		boolean flag = (file.length()!=0);

		// TODO Auto-generated method stub
		return flag;
	}

}
