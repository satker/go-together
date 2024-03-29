import React from 'react';
import PropTypes from "prop-types";
import CardMedia from "@material-ui/core/CardMedia";
import FormHelperText from '@material-ui/core/FormHelperText';

import {getSrcForImg} from "forms/utils/utils";
import ImageSelector from "forms/utils/components/ImageSelector";
import ContainerColumn from "forms/utils/components/Container/ContainerColumn";
import ItemContainer from "forms/utils/components/Container/ItemContainer";
import {PhotoType} from "forms/utils/types";

const PhotoField = ({name, value, setValue, error}) => {
    return <ItemContainer>
        <ContainerColumn style={{
            width: '350px', border: '1px solid ' +
                (error ? 'red' : '#dee2e6')
        }}>
            <ItemContainer>
                <CardMedia style={{height: '255px', width: '350px'}}
                           component="img"
                           image={value && getSrcForImg(value[0])}/>
            </ItemContainer>
            <ItemContainer>
                <ImageSelector photos={value}
                               setPhotos={photo => {
                                   setValue(name, [photo])
                               }}
                               multiple={false}
                />
            </ItemContainer>
        </ContainerColumn>
        <ItemContainer>
            {error && <FormHelperText error>{error}</FormHelperText>}
        </ItemContainer>
    </ItemContainer>;
}

PhotoField.propTypes = {
    name: PropTypes.string.isRequired,
    value: PropTypes.arrayOf(PhotoType),
    setValue: PropTypes.func,
    error: PropTypes.string
};

export default PhotoField;