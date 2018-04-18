import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
// tslint:disable-next-line:no-unused-variable
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FaBan, FaFloppyO, FaArrowLeft } from 'react-icons/lib/fa';

import { getEntity, updateEntity, createEntity, reset } from './customer-addr.reducer';
import { ICustomerAddr } from 'app/shared/model/customer-addr.model';
// tslint:disable-next-line:no-unused-variable
import { convertDateTimeFromServer } from 'app/shared/util/date-utils';

export interface ICustomerAddrUpdateProps {
  getEntity: ICrudGetAction<ICustomerAddr>;
  updateEntity: ICrudPutAction<ICustomerAddr>;
  createEntity: ICrudPutAction<ICustomerAddr>;
  customerAddr: ICustomerAddr;
  reset: Function;
  loading: boolean;
  updating: boolean;
  match: any;
  history: any;
}

export interface ICustomerAddrUpdateState {
  isNew: boolean;
}

export class CustomerAddrUpdate extends React.Component<ICustomerAddrUpdateProps, ICustomerAddrUpdateState> {
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
      const { customerAddr } = this.props;
      const entity = {
        ...customerAddr,
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
    this.props.history.push('/entity/customer-addr');
  };

  render() {
    const isInvalid = false;
    const { customerAddr, loading, updating } = this.props;
    const { isNew } = this.state;

    return (
      <div>
        <Row className="justify-content-center">
          <Col md="8">
            <h2 id="jhi-customer-addr-heading">
              <Translate contentKey="jhipsterMonolithicSampleApp.customerAddr.home.createOrEditLabel">
                Create or edit a CustomerAddr
              </Translate>
            </h2>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="8">
            {loading ? (
              <p>Loading...</p>
            ) : (
              <AvForm model={isNew ? {} : customerAddr} onSubmit={this.saveEntity}>
                {!isNew ? (
                  <AvGroup>
                    <Label for="id">
                      <Translate contentKey="global.field.id">ID</Translate>
                    </Label>
                    <AvInput type="text" className="form-control" name="id" required readOnly />
                  </AvGroup>
                ) : null}
                <AvGroup>
                  <Label id="customerIdLabel" for="customerId">
                    <Translate contentKey="jhipsterMonolithicSampleApp.customerAddr.customerId">Customer Id</Translate>
                  </Label>
                  <AvField type="number" className="form-control" name="customerId" />
                </AvGroup>
                <Button tag={Link} id="cancel-save" to="/entity/customer-addr" replace color="info">
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
  customerAddr: storeState.customerAddr.entity,
  loading: storeState.customerAddr.loading,
  updating: storeState.customerAddr.updating
});

const mapDispatchToProps = {
  getEntity,
  updateEntity,
  createEntity,
  reset
};

export default connect(mapStateToProps, mapDispatchToProps)(CustomerAddrUpdate);
