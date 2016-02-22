package com.medzone.mcloud.measure;

import com.medzone.framework.data.controller.AbstractController;

/**
 * @author hyc
 */
public class NewRuleController extends AbstractController<NewRuleCache> {
    private static NewRuleController controller;

    private NewRuleController() {
    }

    public synchronized static NewRuleController getInstance() {
        if (controller == null) {
            controller = new NewRuleController();
        }
        return controller;
    }


    @Override
    protected NewRuleCache createCache() {
        return new NewRuleCache();
    }

}
