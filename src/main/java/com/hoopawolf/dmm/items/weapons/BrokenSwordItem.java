package com.hoopawolf.dmm.items.weapons;

import com.hoopawolf.dmm.tab.VRMItemGroup;
import net.minecraft.item.Item;

public class BrokenSwordItem extends Item
{
    public BrokenSwordItem(Properties properties)
    {
        super(properties.group(VRMItemGroup.instance));
    }
}
