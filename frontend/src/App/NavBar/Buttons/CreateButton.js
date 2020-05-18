import React from "react";
import PropTypes from "prop-types";
import {navigate} from "hookrouter";
import Badge from "@material-ui/core/Badge";
import AddCircle from "@material-ui/icons/AddCircle";
import IconButton from "@material-ui/core/IconButton";

const CreateButton = ({menuId}) => {
    return <IconButton onClick={() => navigate('/create', true)}
                       key={menuId + '_create'}
                       aria-label="show 4 new mails"
                       color="inherit">
        <Badge color="secondary">
            <AddCircle/>
        </Badge>
    </IconButton>
};

CreateButton.propTypes = {
    menuId: PropTypes.string.isRequired
};

export default CreateButton;