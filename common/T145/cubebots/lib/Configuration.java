package T145.cubebots.lib;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;

import T145.cubebots.CubeBots;
import cpw.mods.fml.client.FMLClientHandler;

public class Configuration {
	public File configFile;
	public Map<String, String> datas = new LinkedHashMap();
	public String[] mod;
	public int commentsCount = 0;

	public Configuration() {}

	public Configuration(File file) {
		loadUsing(file);
	}

	public Configuration(File file, String... m) {
		loadUsing(file);
		setConfigHeader(m);
	}

	public void setConfigHeader(String... m) {
		mod = m;
	}

	public void loadUsing(File file) {
		configFile = file;
		try {
			configFile.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
		load();
	}

	public void load() {
		List<String> readDatas = new ArrayList();
		try {
			FileInputStream fstream = new FileInputStream(configFile);
			DataInputStream in = new DataInputStream(fstream);
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String s;
			while ((s = br.readLine()) != null) readDatas.add(s);
			in.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		if (readDatas.isEmpty()) return;

		boolean halt = false;
		for (int a = 0; a < readDatas.size(); a++) {
			if (a == 0) continue;
			String s = readDatas.get(a);
			int index = s.indexOf(':');
			if (s.length() < 1 || isCommentString(s)) {
				datas.put("#" + commentsCount, s);
				commentsCount++;
			} else if (index > 0) {
				datas.put(s.substring(0, index), s.substring(index + 1).trim());
			} else {
				CubeBots.logger.log(Level.SEVERE, "Unable to read line " + a + "of config file(" + configFile.getAbsolutePath() + ").");
				halt = true;
			}
		}
		if (halt) FMLClientHandler.instance().haltGame("Config file error.", new Exception("Config file error."));
	}

	public void save() {
		try {
			FileWriter write = new FileWriter(configFile);
			PrintWriter out = new PrintWriter(write);
			Iterator<String> ite = datas.keySet().iterator();
			String first = "------------Configuration file------------";
			if (mod.length > 0) {
				first = "------------";
				for (String s : mod) first += s + " ";
				first += "configuration file------------";
			}
			out.println(first);
			while (ite.hasNext()) {
				String s = ite.next();
				if (s.startsWith("#")) out.println(datas.get(s)); else out.println(s + ": " + datas.get(s));
			}
			out.close();
		} catch (IOException e) {
			CubeBots.logger.log(Level.SEVERE, "Failed to save config file: " + configFile.getAbsolutePath());
			e.printStackTrace();
		}
	}

	public boolean isCommentString(String s) {
		return s.startsWith("//") || s.startsWith("#") || s.startsWith("--");
	}

	public void addComment(String s) {
		addComment(1, s, 1);
	}

	/**
	 * Add comment with different starter. 0 or 1 or 2 : # or // or -- .And get
	 * maximum amount of repeated comment
	 **/
	public void addComment(int type, String s, int maxAppearance) {
		String commentStarter = "#";
		if (type == 1) commentStarter = "//";
		if (type == 2) commentStarter = "--";
		int appeared = 0;
		if (maxAppearance > 0) {
			for (String s1 : datas.values()) {
				if (s1.replaceFirst("#", "").replaceFirst("//", "").replaceFirst("--", "").compareTo(s) == 0) appeared++;
				if (appeared >= maxAppearance) return;
			}
		}
		datas.put("#" + commentsCount, commentStarter + s);
		commentsCount++;
	}

	public String getString(String s, String v, String c) {
		if (datas.containsKey(s)) {
			List<String> keys = new ArrayList();
			List<String> values = new ArrayList();
			keys.addAll(datas.keySet());
			values.addAll(datas.values());
			boolean modified = false;
			if (keys.size() < 2) {
				keys.add(0, "#" + commentsCount);
				commentsCount++;
				values.add(0, "//" + c);
				datas.clear();
				modified = true;
			} else {
				int index = keys.indexOf(s);
				boolean add = true;
				String prevValue = values.get(index - 1);
				if (isCommentString(prevValue)) {
					if (prevValue.startsWith("#")) prevValue = prevValue.replaceFirst("#", "");
					if (prevValue.startsWith("//")) prevValue = prevValue.replaceFirst("//", "");
					if (prevValue.startsWith("--")) prevValue = prevValue.replaceFirst("--", "");
					if (c.trim().compareTo(prevValue.trim()) == 0) add = false;
				}
				if (add) {
					keys.add(index, "#" + commentsCount);
					commentsCount++;
					values.add(index, "//" + c);
					datas.clear();
					modified = true;
				}
			}

			if (modified) for (int count = 0; count < Math.min(keys.size(), values.size()); count++) datas.put(keys.get(count), values.get(count));
			return getString(s, v);
		} else {
			setString(s, v, c);
			return v;
		}
	}

	public String getString(String s, String v) {
		if (datas.containsKey(s)) return datas.get(s);
		else {
			setString(s, v);
			return v;
		}
	}

	public void setString(String s, String v, String c) {
		setString(s, v);

		List<String> keys = new ArrayList();
		List<String> values = new ArrayList();
		keys.addAll(datas.keySet());
		values.addAll(datas.values());
		boolean modified = false;
		if (keys.size() < 2) {
			keys.add(0, "#" + commentsCount);
			commentsCount++;
			values.add(0, "//" + c);
			datas.clear();
			modified = true;
		} else {
			int index = keys.indexOf(s);
			boolean add = true;
			String prevValue = values.get(index - 1);
			if (isCommentString(prevValue)) {
				if (prevValue.startsWith("#")) prevValue = prevValue.replaceFirst("#", "");
				if (prevValue.startsWith("//")) prevValue = prevValue.replaceFirst("//", "");
				if (prevValue.startsWith("--")) prevValue = prevValue.replaceFirst("--", "");
				if (c.trim().compareTo(prevValue.trim()) == 0) add = false;
			}
			if (add) {
				keys.add(index, "#" + commentsCount);
				commentsCount++;
				values.add(index, "//" + c);
				datas.clear();
				modified = true;
			}
		}

		if (modified) for (int count = 0; count < Math.min(keys.size(), values.size()); count++) datas.put(keys.get(count), values.get(count));
	}

	public void setString(String s, String v) {
		datas.put(s, v);
	}

	public boolean getBool(String s, boolean v, String c) {
		return Boolean.valueOf(getString(s, String.valueOf(v), c));
	}

	public boolean getBool(String s, boolean v) {
		return Boolean.valueOf(getString(s, String.valueOf(v)));
	}

	public void setBool(String s, boolean v, String c) {
		setString(s, String.valueOf(v), c);
	}

	public void setBool(String s, boolean v) {
		setString(s, String.valueOf(v));
	}

	public byte getByte(String s, byte v, String c) {
		return Byte.parseByte(getString(s, String.valueOf(v), c));
	}

	public byte getByte(String s, byte v) {
		return Byte.parseByte(getString(s, String.valueOf(v)));
	}

	public void setByte(String s, byte v, String c) {
		setString(s, String.valueOf(v), c);
	}

	public void setByte(String s, byte v) {
		setString(s, String.valueOf(v));
	}

	public int getInt(String s, int v, String c) {
		return Integer.parseInt(getString(s, String.valueOf(v), c));
	}

	public int getInt(String s, int v) {
		return Integer.parseInt(getString(s, String.valueOf(v)));
	}

	public void setInt(String s, int v, String c) {
		setString(s, String.valueOf(v), c);
	}

	public void setInt(String s, int v) {
		setString(s, String.valueOf(v));
	}

	public double getDouble(String s, double v, String c) {
		return Double.valueOf(getString(s, String.valueOf(v), c));
	}

	public double getDouble(String s, double v) {
		return Double.valueOf(getString(s, String.valueOf(v)));
	}

	public void setDouble(String s, double v, String c) {
		setString(s, String.valueOf(v), c);
	}

	public void setDouble(String s, double v) {
		setString(s, String.valueOf(v));
	}
}