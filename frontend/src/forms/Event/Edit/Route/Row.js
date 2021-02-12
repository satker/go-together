import React from "react";
import PropTypes from "prop-types";

import {makeStyles} from '@material-ui/core/styles';
import Accordion from '@material-ui/core/Accordion';
import AccordionDetails from '@material-ui/core/AccordionDetails';
import AccordionSummary from '@material-ui/core/AccordionSummary';
import Typography from '@material-ui/core/Typography';
import ExpandMoreIcon from '@material-ui/icons/ExpandMore';
import Divider from '@material-ui/core/Divider';
import LabeledInput from "forms/utils/components/LabeledInput";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import moment from "moment";
import Container from "forms/utils/components/Container/ContainerRow";
import {connect} from "App/Context";
import SelectBox from "forms/utils/components/SelectBox";
import SingleDate from "forms/utils/components/SingleDatePicker";
import {NamedType, RouteInfoItem} from "forms/utils/types";

const useStyles = makeStyles((theme) => ({
    root: {
        width: '100%',
    },
    heading: {
        fontSize: theme.typography.pxToRem(15),
    },
    secondaryHeading: {
        fontSize: theme.typography.pxToRem(15),
        color: theme.palette.text.secondary,
    },
    icon: {
        verticalAlign: 'bottom',
        height: 20,
        width: 20,
    },
    details: {
        alignItems: 'center',
    },
    column: {
        flexBasis: '33.33%',
    },
    helper: {
        borderLeft: `2px solid ${theme.palette.divider}`,
        padding: theme.spacing(1, 2),
    },
    link: {
        color: theme.palette.primary.main,
        textDecoration: 'none',
        '&:hover': {
            textDecoration: 'underline',
        },
    },
}));

const RouteItem = ({route, onChange, transportTypes}) => {
    const classes = useStyles();

    return (
        <div className={classes.root}>
            <Accordion defaultExpanded>
                <AccordionSummary
                    expandIcon={<ExpandMoreIcon/>}
                    aria-controls="panel1c-content"
                    id="panel1c-header"
                >
                    <div className={classes.column}>
                        <Typography className={classes.heading}>{route.location.address}</Typography>
                    </div>
                </AccordionSummary>
                <AccordionDetails className={classes.details}>
                    <Container>
                        <ItemContainer>
                            <SelectBox onChange={(value) => onChange('transportType', value)}
                                       labelText='Transport type'
                                       value={route.transportType}
                                       items={transportTypes}/>
                        </ItemContainer>
                        <ItemContainer>
                            <LabeledInput
                                type='number'
                                id="cost"
                                label="Cost"
                                value={route.cost}
                                onChange={(value) => onChange('cost', value)}
                            />
                        </ItemContainer>
                        <ItemContainer>
                            <LabeledInput
                                type='number'
                                id="movementDuration"
                                label="Movement duration"
                                value={route.movementDuration}
                                onChange={(value) => onChange('movementDuration', value)}
                            />
                        </ItemContainer>
                        <ItemContainer>
                            <SingleDate date={route.movementDate && moment(route.movementDate)}
                                        onChange={movementDate => onChange('movementDate', movementDate)}
                                        label='Movement date'
                            />
                        </ItemContainer>
                    </Container>
                </AccordionDetails>
                <Divider/>
            </Accordion>
        </div>
    );
};

RouteItem.propTypes = {
    route: RouteInfoItem,
    onChange: PropTypes.func.isRequired,
    transportTypes: PropTypes.arrayOf(NamedType)
};

const mapStateToProps = (state) => ({
    transportTypes: state.components.forms.event.eventEdit.transportTypes.response
});

export default connect(mapStateToProps, null)(RouteItem)