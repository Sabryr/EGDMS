/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package no.ntnu.medisin.egenvar.server;

import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author sabryr
 */
public class Scripts {

    public static void main(String[] args) {
     FileWriter out = null;
        try {
            out = new FileWriter("/home/sabryr/Python/fromJava.py");
            out.append(getPython("http://ans-180230.stolav.ntnu.no:8085/eGenVar_WebService/Authenticate_service?wsdl"));
            out.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                out.close();

            } catch (IOException ex) {
            }
        }
    }

    public static String getPython(String wsdl) {
        String pysc = "import suds\n"
                + "import hashlib\n"
                + "import os.path\n"
                + "import getpass\n"
                + "from sys import version_info\n"
                + "from suds.client import Client\n"
                + "\n"
                + "class eGenPy:\n"
                + "    'This is an example client connecting to the server "+wsdl.substring(0, wsdl.indexOf("eGenVar"))+" generated by eGenScriptor@"+Timing.getDateTime()+" '\n"
                + "    # The WSDL URL\n"
                + "    url = '" + wsdl + "'\n"
                + "    #Intiate client\n"
                + "    client = Client(url)\n"
                + "    \n"
                + "    def __init__(self):\n"
                + "        A=1\n"
                + "    \n"
                + "        \n"
                + "    # Check python version\n"
                + "    is_py2 = version_info[0] < 3\n"
                + "\n"
                + "    # This function performs the authentication against the EGDMS server.\n"
                + "    # The user name is the email address used during registration and the \n"
                + "    # authfile is a file to store the authentication key, so that the user\n"
                + "    # do not have to enter the username and password all the time. \n"
                + "    def authenticate(self,username, authfile):\n"
                + "        if(os.path.isfile(authfile)):\n"
                + "            f = open(authfile, 'r')\n"
                + "            val_a= [f.readline(),f.readline()]\n"
                + "            f.close()\n"
                + "            #This array contains the user name and the authentication key. \n"
                + "            return val_a;\n"
                + "        else:\n"
                + "            #if the authentication file was not found, perform a new authentication\n"
                + "            if is_py2:\n"
                + "                user_name = raw_input('Please enter user name(usually email address\"): ')\n"
                + "            else:\n"
                + "                user_name = input('Please enter user name(usually email address\"): ')\n"
                + "                password =getpass.getpass(\"Please enter password: \") \n"
                + "                result = client.service.Authenticate(user_name,hashlib.sha1(password).hexdigest())\n"
                + "                if(result=='0'):\n"
                + "                    #Authentication failed\n"
                + "                    return None;\n"
                + "                else:\n"
                + "                    # Write the username and authentication key to a file.\n"
                + "                    f = open(authfile, 'wb')\n"
                + "                    f.write(user_name+'\\n')\n"
                + "                    f.write(result)\n"
                + "                    f.close()\n"
                + "                    val_a=[user_name,result]\n"
                + "                    return val_a;\n"
                + "    \n"
                + "    \n"
                + "    #This function can search for the specified query (source) and return the results\n"
                + "    #(target).\n"
                + "    def search(self,username, authfile,query, target, exactonly, type):\n"
                + "        # authenticate is the function described in box 8.1. The authfile is the file with \n"
                + "        # the authentication key. if not found a new file will be created. \n"
                + "        val_a=self.authenticate(username,authfile); \n"
                + "        if val_a is not None:\n"
                + "            #Encrypt the server key\n"
                + "            s_key= hashlib.sha1(val_a[1]).hexdigest()\n"
                + "            # argument 1 = the encrypted server key\n"
                + "            # argument 2 = username\n"
                + "            # argument 3 = search query\n"
                + "            # argument 4 = return type\n"
                + "            # argument 5 = Whether to perform an exact match\n"
                + "            # argument 6 = Query type refer the 4.3. Search for more details\n"
                + "            return self.client.service.search(s_key,val_a[0],query,target,exactonly, type)   \n"
                + "    \n"
                + "    #The user name and authentication key file are for the authentication process. The #param_l is a list with the parameters to add\n"
                + "    def add(self,username, authfile,param_l):\n"
                + "        # authenticate is the function described in box 8.1. The authfile is the file with \n"
                + "        # the authentication key. if not found one will be created. \n"
                + "        val_a=self.authenticate('user','authfile');\n"
                + "        if val_a is not None:\n"
                + "            #Encrypt the server key\n"
                + "            s_key= hashlib.sha1(val_a[1]).hexdigest()\n"
                + "            return self.client.service.add(s_key,val_a[0],param_l)\n"
                + "    \n"
                + "\n"
                + "    # authenticate is the function described in box 8.1. The authfile is the file with \n"
                + "    # the authentication key. if not found one will be created. \n"
                + "    def relate(self, username, authfile,relationships):\n"
                + "        val_a=self.authenticate('user','authfile');\n"
                + "        if val_a is not None:\n"
                + "            #Encrypt the server key\n"
                + "            s_key= hashlib.sha1(val_a[1]).hexdigest()\n"
                + "            #Relationships between files are constructed using their SHA-1 checksum values\n"
                + "            return self.client.service.relate(s_key,val_a[0],relationships)  \n"
                + "        \n"
                + "    \n"
                + "    \n"
                + "    # authenticate is the function described in box 8.1. The authfile is the file with \n"
                + "    # the authentication key. if not found one will be created. \n"
                + "    def tag(self, username, authfile, tag_target, tagdetails):\n"
                + "        val_a=self.authenticate('user','authfile');\n"
                + "        if val_a is not None:\n"
                + "            #Encrypt the server key\n"
                + "            s_key= hashlib.sha1(val_a[1]).hexdigest()\n"
                + "            return self.client.service.tag(s_key,val_a[0],tag_target,tagdetails)  \n"
                + "    \n"
                + "    \n"
                + "    # authenticate is the function described in box 8.1. The authfile is the file with \n"
                + "    # the authentication key. if not found one will be created. \n"
                + "    # drc is a the Delete request code and can be otained by issuing the command\n"
                + "    # client.service.delete(s_key,val_a[0],\"DRC\", NULL)\n"
                + "    def update(self, username, authfile,param_l):\n"
                + "        val_a=self.authenticate('user','authfile');\n"
                + "        if val_a is not None:\n"
                + "            #Encrypt the server key\n"
                + "            s_key= hashlib.sha1(val_a[1]).hexdigest()\n"
                + "            #Relationships between files are constructed using their SHA-1 checksum values\n"
                + "            return self.client.service.update(s_key, val_a[0], param_l)\n"
                + "    \n"
                + "    \n"
                + "    # authenticate is the function described in box 8.1. The authfile is the file with \n"
                + "    # the authentication key. if not found one will be created. \n"
                + "    # drc is a the Delete request code and can be otained by issuing the command\n"
                + "    # client.service.delete(s_key,val_a[0],\"DRC\", NULL)\n"
                + "    def delete(self, username, authfile,param_l):\n"
                + "        val_a=self.authenticate('user','authfile');\n"
                + "        if val_a is not None:\n"
                + "            #Encrypt the server key\n"
                + "            s_key= hashlib.sha1(val_a[1]).hexdigest()\n"
                + "            #Relationships between files are constructed using their SHA-1 checksum values\n"
                + "            return self.client.service.delete(s_key,val_a[0],param_l)";

        return pysc;
    }
}