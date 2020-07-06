import React from 'react';
import CardMedia from "@material-ui/core/CardMedia";

import {getSrcForImg} from "forms/utils/utils";
import ErrorMessage from "forms/utils/components/LoadableContent/ErrorMessage";
import ImageSelector from "forms/utils/components/ImageSelector";
import ContainerColumn from "../../Container/ContainerColumn";
import ItemContainer from "../../Container/ItemContainer";

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
            {error && <ErrorMessage error={error}/>}
        </ItemContainer>
    </ItemContainer>;
}

export default PhotoField;