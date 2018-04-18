import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FaArrowLeft, FaPencil } from 'react-icons/lib/fa';

import { getEntity } from './customer-addr.reducer';
import { ICustomerAddr } from 'app/shared/model/customer-addr.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ICustomerAddrDetailProps {
  getEntity: ICrudGetAction<ICustomerAddr>;
  customerAddr: ICustomerAddr;
  match: any;
}

export class CustomerAddrDetail extends React.Component<ICustomerAddrDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { customerAddr } = this.props;
    return (
      <div className="row justify-content-center">
        <div className="col-8">
          <h2>
            <Translate contentKey="jhipsterMonolithicSampleApp.customerAddr.detail.title">CustomerAddr</Translate> [<b>{customerAddr.id}</b>]
          </h2>
          <dl className="row-md jh-entity-details">
            <dt>
              <span id="customerId">
                <Translate contentKey="jhipsterMonolithicSampleApp.customerAddr.customerId">Customer Id</Translate>
              </span>
            </dt>
            <dd>{customerAddr.customerId}</dd>
          </dl>
          <Button tag={Link} to="/entity/customer-addr" replace color="info">
            <FaArrowLeft />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          <Button tag={Link} to={`/entity/customer-addr/${customerAddr.id}/edit`} replace color="primary">
            <FaPencil />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.edit">Edit</Translate>
            </span>
          </Button>
        </div>
      </div>
    );
  }
}

const mapStateToProps = ({ customerAddr }) => ({
  customerAddr: customerAddr.entity
});

const mapDispatchToProps = { getEntity };

export default connect(mapStateToProps, mapDispatchToProps)(CustomerAddrDetail);
