/**************************************************************************************
 * bias_tree
 * Copyright (c) 2014-2017 National University of Colombia, https://github.com/remixlab
 * @author Jean Pierre Charalambos, http://otrolado.info/
 *
 * All rights reserved. Library that eases the creation of interactive
 * scenes, released under the terms of the GNU Public License v3.0
 * which is available at http://www.gnu.org/licenses/gpl.html
 **************************************************************************************/

package remixlab.bias.event;

import remixlab.bias.Event;
import remixlab.bias.Shortcut;

/**
 * This class represents {@link TapEvent} shortcuts.
 * <p>
 * Click shortcuts are defined with a specific number of clicks and can be of one out of
 * two forms: 1. A gesture-id; and, 2. A gesture-id plus a key-modifier (such as the CTRL
 * key).
 * <p>
 * Note that click shortcuts should have at least one click.
 */
public class TapShortcut extends Shortcut {
  protected int _count;

  /**
   * Defines a single click shortcut from the given gesture-id.
   *
   * @param id id
   */
  public TapShortcut(int id) {
    this(Event.NO_MODIFIER_MASK, id, 1);
  }

  /**
   * Defines a click shortcut from the given gesture-id and number of taps.
   *
   * @param id id
   * @param count  number of taps
   */
  public TapShortcut(int id, int count) {
    this(Event.NO_MODIFIER_MASK, id, count);
  }

  /**
   * Defines a click shortcut from the given gesture-id, modifier mask, and number of
   * clicks.
   *
   * @param modifiers  modifier mask
   * @param id id
   * @param count  number of taps
   */
  public TapShortcut(int modifiers, int id, int count) {
    super(modifiers, id);
    if (count <= 0)
      this._count = 1;
    else
      this._count = count;
  }

  /**
   * Returns the click-shortcut click count.
   */
  public int count() {
    return _count;
  }

  @Override
  public boolean matches(Shortcut other) {
    if(other instanceof TapShortcut)
      return super.matches(other) && count() == ((TapShortcut) other).count();
    return false;
  }
}
