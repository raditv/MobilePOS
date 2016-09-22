package com.odoo.pos.views.component;

import android.support.v4.app.Fragment;

/**
 * Fragment which is able to call update() from other class.
 * This is used by Delegation pattern.
 * 
 * @author odoo Team
 *
 */
public abstract class UpdatableFragment extends Fragment {

	/**
	 * Update fragment.
	 */
	public abstract void update();

}
