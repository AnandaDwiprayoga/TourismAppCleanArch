package id.pasukanlangit.maps.di

import android.content.Context
import com.dicoding.tourismapp.di.MapsModuleDepedency
import dagger.BindsInstance
import dagger.Component
import id.pasukanlangit.maps.MapsActivity

@Component(dependencies = [MapsModuleDepedency::class])
interface MapsComponent {
    fun inject(activity: MapsActivity)

    @Component.Builder
    interface Builder {
        fun context(@BindsInstance context: Context): Builder
        fun appDependencies(mapsModuleDependencies: MapsModuleDepedency): Builder
        fun build(): MapsComponent
    }
}