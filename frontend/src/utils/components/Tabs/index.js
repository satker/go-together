import React, {useState} from "react";
import {Nav, TabContent, TabPane} from "reactstrap";
import NavTab from "./NavTab";
import PropTypes from "prop-types";

const ElementTabs = ({tabs, elements, elementsFieldTab, Form}) => {
    const [activeTab, setActiveTab] = useState(tabs[0]);

    const toggle = (tab) => {
        if (activeTab !== tab) {
            setActiveTab(tab);
        }
    };

    return <div className='flex'>
        <Nav tabs>
            {tabs.map((tab) => <NavTab name={tab}
                                       activeTab={activeTab}
                                       toggle={toggle}/>)}
        </Nav>
        <TabContent activeTab={activeTab}>
            {elements.map(element => <TabPane tabId={element[elementsFieldTab]}>
                <Form item={element}/>
            </TabPane>)}
        </TabContent>
    </div>
};

ElementTabs.propTypes = {
    tabs: PropTypes.array.isRequired,
    elements: PropTypes.arrayOf(PropTypes.object).isRequired,
    elementsFieldTab: PropTypes.string.isRequired,
    Form: PropTypes.object.isRequired
};

export default ElementTabs;