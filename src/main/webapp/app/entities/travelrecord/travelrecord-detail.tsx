import * as React from 'react';
import { connect } from 'react-redux';
import { Link } from 'react-router-dom';
import { Button } from 'reactstrap';
// tslint:disable-next-line:no-unused-variable
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FaArrowLeft, FaPencil } from 'react-icons/lib/fa';

import { getEntity } from './travelrecord.reducer';
import { ITravelrecord } from 'app/shared/model/travelrecord.model';
// tslint:disable-next-line:no-unused-variable
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ITravelrecordDetailProps {
  getEntity: ICrudGetAction<ITravelrecord>;
  travelrecord: ITravelrecord;
  match: any;
}

export class TravelrecordDetail extends React.Component<ITravelrecordDetailProps> {
  componentDidMount() {
    this.props.getEntity(this.props.match.params.id);
  }

  render() {
    const { travelrecord } = this.props;
    return (
      <div className="row justify-content-center">
        <div className="col-8">
          <h2>
            <Translate contentKey="jhipsterMonolithicSampleApp.travelrecord.detail.title">Travelrecord</Translate> [<b>{travelrecord.id}</b>]
          </h2>
          <dl className="row-md jh-entity-details">
            <dt>
              <span id="name">
                <Translate contentKey="jhipsterMonolithicSampleApp.travelrecord.name">Name</Translate>
              </span>
            </dt>
            <dd>{travelrecord.name}</dd>
            <dt>
              <span id="phone">
                <Translate contentKey="jhipsterMonolithicSampleApp.travelrecord.phone">Phone</Translate>
              </span>
            </dt>
            <dd>{travelrecord.phone}</dd>
          </dl>
          <Button tag={Link} to="/entity/travelrecord" replace color="info">
            <FaArrowLeft />{' '}
            <span className="d-none d-md-inline">
              <Translate contentKey="entity.action.back">Back</Translate>
            </span>
          </Button>
          <Button tag={Link} to={`/entity/travelrecord/${travelrecord.id}/edit`} replace color="primary">
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

const mapStateToProps = ({ travelrecord }) => ({
  travelrecord: travelrecord.entity
});

const mapDispatchToProps = { getEntity };

export default connect(mapStateToProps, mapDispatchToProps)(TravelrecordDetail);
