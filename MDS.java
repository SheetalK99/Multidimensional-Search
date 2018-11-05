package sak170006;

/*
Team members: (LP1)
Akshaya Ramaswamy (axr170131)
Sheetal Kadam (sak170006)
Meghna Mathur (mxm180022)
Maleeha Koul  (msk180001)
 
 */

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeMap;

// If you want to create additional classes, place them in this file as subclasses of MDS

public class MDS {

	class MDSEntry {

		Money price;
		HashSet<Long> description;
		
		/**
		 * Constructor for the class Entry
		**/
		
		MDSEntry(Money price, List<Long> list) {
			this.price = price;
			this.description = new HashSet<>(list);
		}

		
		public HashSet<Long> getDescription() {
			return this.description;
		}

		public void setDescription(HashSet<Long> desc) {
			this.description = desc;
		}

		public void setPrice(Money price) {
			this.price = price;
		}

		public Money getPrice() {
			return this.price;
		}

	}
	// Add fields of MDS here

	TreeMap<Long, MDSEntry> tree;
	HashMap<Long, HashSet<Long>> descTable; // table for Description Search

	/**
	 * Constructor MDS
	 */ 
	public MDS() {
		tree = new TreeMap<>();
		descTable = new HashMap<>();

	}

	public MDSEntry getEntry(Long id) {
		return tree.get(id);
	}

	public void removeFromDescTable(Long desc) {
		descTable.remove(desc);
	}

	public HashSet<Long> getFromDescTable(Long desc) {
		return descTable.get(desc);
	}

	public void putInDescTable(Long desc, HashSet<Long> ids) {

		descTable.put(desc, ids);
	}

	public boolean contains(Long id) {
		return tree.containsKey(id);
	}

	/*
	 * Public methods of MDS. Do not change their signatures. ________ a.
	 * Insert(id,price,list): insert a new item whose description is given in the
	 * list. If an entry with the same id already exists, then its description and
	 * price are replaced by the new values, unless list is null or empty, in which
	 * case, just the price is updated. Returns 1 if the item is new, and 0
	 * otherwise.
	 */

	public int insert(long id, Money price, java.util.List<Long> list) {
		int return_val = 1;

		// convert description list to hashlist
		HashSet<Long> list_new;
		if (list != null) {
			list_new = new HashSet<>(list);
		} else {
			list_new = new HashSet<>();
		}
		if (contains(id)) {
			// replace
			if (list != null && !list.isEmpty()) {

				for (long x : getEntry(id).getDescription()) {

					if (list_new.contains(x)) {
						continue; // description id exists in new description so no need to remove
					} else {
						HashSet<Long> t = getFromDescTable(x); // get list of ids from desc table 
																//to be removed
						if (t.size() > 1) {
							t.remove(id);

						} else {
							removeFromDescTable(id); // only 1 entry
						}

					}
				}
				// update description
				getEntry(id).setDescription(list_new);

			}

			// set price to new price
			getEntry(id).setPrice(price);

			return_val = 0;
		}

		else {

			// New entry
			tree.put(id, new MDSEntry(price, list));
		}

		// Add new desc ids to desc table
		if (list_new != null && !list_new.isEmpty()) {

			for (long x : list_new) {

				HashSet<Long> s2 = getFromDescTable(x);

				if (s2 == null) { // new desc id

					HashSet<Long> temp = new HashSet<>();
					//add new desc  
					temp.add(id);
					putInDescTable(x, temp);
				} else {
					s2.add(id); // append id to already existing desc id
				}

			}
		}

		return return_val;
	}

	// b. Find(id): return price of item with given id (or 0, if not found).
	public Money find(long id) {
		if (getEntry(id) != null) {

			return getEntry(id).getPrice();
		} else {
			return new Money();
		}
	}

	/*
	 * c. Delete(id): delete item from storage. Returns the sum of the long ints
	 * that are in the description of the item deleted, or 0, if such an id did not
	 * exist.
	 */
	public long delete(long id) {
		long sum = 0;

		if (contains(id)) {
			MDSEntry rm = tree.remove(id);
			if (rm != null) {

				// Description
				for (long x : rm.getDescription()) {
					// update sum of deleted items
					sum = sum + x;
					HashSet<Long> s2 = getFromDescTable(x);
					if (s2.size() > 1) {
						s2.remove(id);
					} else {
						removeFromDescTable(x); //only 1 entry
					}

				}

			}
			return sum;
		}

		return 0;
	}

	/*
	 * d. FindMinPrice(n): given a long int, find items whose description contains
	 * that number (exact match with one of the long ints in the item's
	 * description), and return lowest price of those items. Return 0 if there is no
	 * such item.
	 */
	public Money findMinPrice(long n) {

		HashSet<Long> t = getFromDescTable(n);
		if (t == null) {
			return new Money();
		} else {
			Money moneymin = new Money(1111, 100);
			for (long x : t) {
				
				Money m = getEntry(x).getPrice();
				
				if (moneymin.compareTo(m) > 0) {
					moneymin = m;

				}
			}
			return moneymin;
		}

	}

	/*
	 * e. FindMaxPrice(n): given a long int, find items whose description contains
	 * that number, and return highest price of those items. Return 0 if there is no
	 * such item.
	 */
	public Money findMaxPrice(long n) {

		HashSet<Long> t = getFromDescTable(n);
	
		if (t == null) {
			return new Money();
		}
		else {
			Money moneymax = new Money(-1, 0);
			for (long x : t) {
				Money m = getEntry(x).getPrice();
				if (moneymax.compareTo(m) < 0) {
					moneymax = m;

				}
			}
			return moneymax;
		}
	}

	/*
	 * f. FindPriceRange(n,low,high): given a long int n, find the number of items
	 * whose description contains n, and in addition, their prices fall within the
	 * given range, [low, high].
	 */
	public int findPriceRange(long n, Money low, Money high) {
		int c = 0; // counter to track no of items

		HashSet<Long> t = getFromDescTable(n);
		if (t != null) {
			for (long x : t) {

				Money m = getEntry(x).getPrice();

				if (m.compareTo(low) >= 0 && m.compareTo(high) <= 0) {
					c++;
				}
			}
		}
		return c;
	}

	/*
	 * g. PriceHike(l,h,r): increase the price of every product, whose id is in the
	 * range [l,h] by r%. Discard any fractional pennies in the new prices of items.
	 * Returns the sum of the net increases of the prices.
	 */
	public Money priceHike(long l, long h, double rate) {
		long increase = 0;

		for (long i = l; i <= h; i++) {
			if (getEntry(i) != null) {

				Money oldm = getEntry(i).getPrice();
				Money newm = new Money();

				double amount_cents = (oldm.dollars() * 100 + oldm.cents()) * (1 + (rate / 100));
				double amount_trunc = Math.round(amount_cents * 100) / 100.00;

				newm.c = (int) (amount_trunc % 100);
				newm.d = (long) ((amount_trunc - newm.c) / 100);
				
				// update increased amount
				increase = increase + newm.difference(oldm);
				insert(i, newm, null); // update main tree 
			}
		}
		// create object of increased Money
		int c = (int) (increase % 100);
		long d = (long) ((increase - c) / 100);
		return new Money(d, c);

	}

	/*
	 * h. RemoveNames(id, list): Remove elements of list from the description of id.
	 * It is possible that some of the items in the list are not in the id's
	 * description. Return the sum of the numbers that are actually deleted from the
	 * description of id. Return 0 if there is no such id.
	 */
	public boolean descContainsId(Long desc, Long id) {
		return getEntry(id).getDescription().contains(desc);
	}

	public void removeIdFromDesc(Long desc, Long id) {
		tree.get(id).description.remove(desc);
	}

	public long removeNames(long id, java.util.List<Long> list) {
		long sum = 0;
		for (long x : list) {
			// if desc table contains id 
			if (descContainsId(x, id)) {
				sum = sum + x;
				removeIdFromDesc(x, id); // remove

				HashSet<Long> product = getFromDescTable(x);

				if (product.size() > 1) {
					product.remove(id);
				} else {
					removeFromDescTable(x); // only 1 entry
				}

			}
		}
		return sum;
	}

	// Do not modify the Money class in a way that breaks LP3Driver.java
	public static class Money implements Comparable<Money> {
		long d;
		int c;

		public Money() {
			d = 0;
			c = 0;
		}

		public Money(long d, int c) {
			this.d = d;
			this.c = c;
		}

		public Money(String s) {
			String[] part = s.split("\\.");
			int len = part.length;
			if (len < 1) {
				d = 0;
				c = 0;
			} else if (part.length == 1) {
				d = Long.parseLong(s);
				c = 0;
			} else {
				d = Long.parseLong(part[0]);
				c = Integer.parseInt(part[1]);
			}
		}

		public long dollars() {
			return d;
		}

		public int cents() {
			return c;
		}

		public int compareTo(Money other) { // Complete this, if needed
			if (this.dollars() > other.dollars())
				return 1;
			else if (this.dollars() == other.dollars()) {
				if (this.cents() == other.cents())
					return 0;
				else if (this.cents() > other.cents())
					return 1;
				else
					return -1;
			} else
				return -1;
		}

		public long difference(Money other) {
			return (((this.dollars() * 100) + this.cents()) - ((other.dollars() * 100) + other.cents()));
		}

		public String toString() {
			return d + "." + c;
		}

	}
}