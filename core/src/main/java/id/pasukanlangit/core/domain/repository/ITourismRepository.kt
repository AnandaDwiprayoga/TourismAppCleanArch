package id.pasukanlangit.core.domain.repository

import id.pasukanlangit.core.data.Resource
import id.pasukanlangit.core.domain.model.Tourism
import kotlinx.coroutines.flow.Flow

interface ITourismRepository {
    fun getAllTourism() : Flow<Resource<List<Tourism>>>
    fun getFavoriteTourism() : Flow<List<Tourism>>
    fun setFavoriteTourism(tourism: Tourism, state: Boolean)
}