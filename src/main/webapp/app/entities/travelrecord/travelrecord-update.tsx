import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FaBan, FaFloppyO, FaArrowLeft } from 'react-icons/lib/fa';

import { getEntity, updateEntity, createEntity, reset } from './travelrecord.reducer';
import { ITravelrecord } from 'app/shared/model/travelrecord.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';

export interface ITravelrecordUpdateProps {
  getEntity: ICrudGetAction<ITravelrecord>;
  updateEntity: ICrudPutAction<ITravelrecord>;
  createEntity: ICrudPutAction<ITravelrecord>;
  travelrecord: ITravelrecord;
  reset: Function;
  loading: boolean;
  updating: boolean;
  match: any;
  history: any;
}

export interface ITravelrecordUpdateState {
  isNew: boolean;
}

export class TravelrecordUpdate extends React.Component<ITravelrecordUpdateProps, ITravelrecordUpdateState> {
  constructor(props) {
    super(props);
    this.state = {
      isNew: !this.props.match.params || !this.props.match.params.id
    };
  }

  componentDidMount() {
    if (this.state.isNew) {
      this.props.reset();
    } else {
      this.props.getEntity(this.props.match.params.id);
    }
  }

  saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const { travelrecord } = this.props;
      const entity = {
        ...travelrecord,
        ...values
      };

      if (this.state.isNew) {
        this.props.createEntity(entity);
      } else {
        this.props.updateEntity(entity);
      }
      this.handleClose();
    }
  };

  handleClose = () => {
    this.props.history.push('/entity/travelrecord');
  };

  render() {
    const isInvalid = false;
    const { travelrecord, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="jhi-travelrecord-heading">
              <Translate contentKey="jhipsterMonolithicSampleApp.travelrecord.home.createOrEditLabel">
                Create or edit a Travelrecord
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : travelrecord} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="nameLabel" for="name">
                    <Translate contentKey="jhipsterMonolithicSampleApp.travelrecord.name">Name</Translate>
                  </Label>
                  <AvField
                    type="text"
                    name="name"
                    validate={{
                      required: { value: true, errorMessage: translate('entity.validation.required') }
                    }}
                  />
                </AvGroup>
                <AvGroup>
                  <Label id="phoneLabel" for="phone">
                    <Translate contentKey="jhipsterMonolithicSampleApp.travelrecord.phone">Phone</Translate>
                  </Label>
                  <AvField type="number" className="form-control" name="phone" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/travelrecord" replace color="info">
                  <FaArrowLeft />&nbsp;
                  <span className="d-none d-md-inline">
                    <Translate contentKey="entity.action.back">Back</Translate>
                  </span>
                </Button>
                &nbsp;
                <Button color="primary" id="save-entity" type="submit" disabled={isInvalid || updating}>
                  <FaFloppyO />&nbsp;
                  <Translate contentKey="entity.action.save">Save</Translate>
                </Button>
              </AvForm>
            )}
          </Col>
        </Row>
      </div>
    );
  }
}

const mapStateToProps = storeState => ({
  travelrecord: storeState.travelrecord.entity,
  loading: storeState.travelrecord.loading,
  updating: storeState.travelrecord.updating
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

export default connect(mapStateToProps, mapDispatchToProps)(TravelrecordUpdate);
